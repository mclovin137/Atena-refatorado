package br.com.atena.escolar.importacao.dao;

import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.importacao.actor.AnalisarRegistroLegadoActor;
import br.com.atena.escolar.importacao.actor.AplicarRegistroLegadoActor;
import br.com.atena.escolar.importacao.actor.PrepararFonteImportacaoActor;
import br.com.atena.escolar.importacao.dominio.EstadoImportacaoEnum;
import br.com.atena.escolar.importacao.dominio.RegistroLegado;
import br.com.atena.escolar.importacao.dto.RegistroImportacaoDTO;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Comparator;
import java.util.UUID;

@Configuration
public class ImportacaoLoteConfig {
    @Bean
    @StepScope
    public ItemReader<RegistroLegado> origemLegadoReader(
            PrepararFonteImportacaoActor preparar,
            @Value("#{jobParameters['importacaoId']}") String id) {
        return new ListItemReader<>(preparar.ler(UUID.fromString(id)));
    }

    @Bean
    @StepScope
    public ItemWriter<RegistroLegado> analisarLegadoWriter(
            AnalisarRegistroLegadoActor analisar,
            IRegistroImportacaoDAO registros,
            @Value("#{jobParameters['importacaoId']}") String id) {
        UUID identificador = UUID.fromString(id);
        return chunk -> {
            try {
                for (var registro : chunk) {
                    registros.guardar(identificador, analisar.executar(registro));
                }
            } catch (RuntimeException erro) {
                throw falhaPrivada();
            }
        };
    }

    @Bean
    public Step analisarLegadoStep(
            JobRepository repository,
            PlatformTransactionManager transacoes,
            @Qualifier("origemLegadoReader") ItemReader<RegistroLegado> leitor,
            @Qualifier("analisarLegadoWriter") ItemWriter<RegistroLegado> escritor) {
        return new StepBuilder("analisarLegadoStep", repository)
                .<RegistroLegado, RegistroLegado>chunk(100, transacoes)
                .reader(leitor)
                .writer(escritor)
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<RegistroImportacaoDTO> planoLegadoReader(
            IRegistroImportacaoDAO registros,
            @Value("#{jobParameters['importacaoId']}") String id) {
        var plano =
                registros.listar(UUID.fromString(id)).stream()
                        .sorted(Comparator.comparingInt(registro -> fase(registro.tabela())))
                        .toList();
        return new ListItemReader<>(plano);
    }

    @Bean
    @StepScope
    public ItemWriter<RegistroImportacaoDTO> aplicarLegadoWriter(
            AplicarRegistroLegadoActor aplicar,
            IRegistroImportacaoDAO registros,
            @Value("#{jobParameters['importacaoId']}") String id,
            @Value("#{jobParameters['reprocessar']}") String reprocessar) {
        UUID identificador = UUID.fromString(id);
        var contexto = new java.util.ArrayList<>(registros.listar(identificador));
        return chunk -> {
            try {
                for (var registro : chunk) {
                    var resultado =
                            aplicar.executar(
                                    identificador, registro, contexto, "true".equals(reprocessar));
                    registros.resultado(identificador, resultado);
                    for (int indice = 0; indice < contexto.size(); indice++) {
                        var origem = contexto.get(indice);
                        if (origem.tabela().equals(resultado.tabela())
                                && origem.ordinal() == resultado.ordinal()) {
                            contexto.set(indice, resultado);
                            break;
                        }
                    }
                }
            } catch (RuntimeException erro) {
                // Não encadear exceções JDBC com detalhes privados em logs do Spring Batch.
                throw falhaPrivada();
            }
        };
    }

    @Bean
    public Step importarLegadoStep(
            JobRepository repository,
            PlatformTransactionManager transacoes,
            @Qualifier("planoLegadoReader") ItemReader<RegistroImportacaoDTO> leitor,
            @Qualifier("aplicarLegadoWriter") ItemWriter<RegistroImportacaoDTO> escritor) {
        return new StepBuilder("importarLegadoStep", repository)
                .<RegistroImportacaoDTO, RegistroImportacaoDTO>chunk(100, transacoes)
                .reader(leitor)
                .writer(escritor)
                .build();
    }

    @Bean
    public Job analisarLegado(
            JobRepository repository,
            @Qualifier("analisarLegadoStep") Step etapa,
            IFonteImportacaoDAO fontes) {
        return new JobBuilder("analisarLegado", repository)
                .start(etapa)
                .listener(conclusao(fontes, true))
                .build();
    }

    @Bean
    public Job importarLegado(
            JobRepository repository,
            @Qualifier("importarLegadoStep") Step etapa,
            IFonteImportacaoDAO fontes) {
        return new JobBuilder("importarLegado", repository)
                .start(etapa)
                .listener(conclusao(fontes, false))
                .build();
    }

    @Bean
    public Job reprocessarLegado(
            JobRepository repository,
            @Qualifier("importarLegadoStep") Step etapa,
            IFonteImportacaoDAO fontes) {
        return new JobBuilder("reprocessarLegado", repository)
                .start(etapa)
                .listener(conclusao(fontes, false))
                .build();
    }

    private JobExecutionListener conclusao(IFonteImportacaoDAO fontes, boolean analise) {
        return new JobExecutionListener() {
            @Override
            public void afterJob(JobExecution execucao) {
                UUID id = UUID.fromString(execucao.getJobParameters().getString("importacaoId"));
                var estado = EstadoImportacaoEnum.FAILED;
                if (execucao.getStatus() == BatchStatus.COMPLETED) {
                    boolean pendencias =
                            fontes.consultar(id).contagens().stream()
                                    .anyMatch(contagem -> !contagem.status().equals("IMPORTADO"));
                    estado =
                            analise
                                    ? EstadoImportacaoEnum.PLANEJADA
                                    : pendencias
                                            ? EstadoImportacaoEnum.CONCLUIDA_COM_PENDENCIAS
                                            : EstadoImportacaoEnum.CONCLUIDA;
                }
                fontes.finalizar(id, estado);
            }
        };
    }

    private static int fase(String tabela) {
        return switch (tabela) {
            case "curso", "materia" -> 0;
            case "turmas" -> 1;
            case "grades" -> 2;
            case "gradecurricular" -> 3;
            case "user", "livros", "evento" -> 4;
            default -> 5;
        };
    }

    private static ErroAplicacao falhaPrivada() {
        return new ErroAplicacao(
                ErroAplicacao.CategoriaEnum.FALHA_INTERNA,
                "Falha ao processar a importação privada");
    }
}
