package br.com.atena.escolar.importacao.actor;

import static br.com.atena.escolar.importacao.helper.ResultadoImportacaoHelper.importado;
import static br.com.atena.escolar.importacao.helper.ResultadoImportacaoHelper.pendente;

import br.com.atena.escolar.importacao.dao.IRegistroImportacaoDAO;
import br.com.atena.escolar.importacao.dto.RegistroImportacaoDTO;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Coordena projeções concretas do schema legado; não interpreta SQL nem cargos. */
@Component
public class AplicarRegistroLegadoActor {
    private final ImportarCatalogosLegadoActor catalogos;
    private final ImportarTurmasLegadoActor turmas;
    private final ImportarGradesLegadoActor grades;
    private final ImportarOfertasLegadoActor ofertas;
    private final IRegistroImportacaoDAO origens;

    public AplicarRegistroLegadoActor(
            ImportarCatalogosLegadoActor catalogos,
            ImportarTurmasLegadoActor turmas,
            ImportarGradesLegadoActor grades,
            ImportarOfertasLegadoActor ofertas,
            IRegistroImportacaoDAO origens) {
        this.catalogos = catalogos;
        this.turmas = turmas;
        this.grades = grades;
        this.ofertas = ofertas;
        this.origens = origens;
    }

    public RegistroImportacaoDTO executar(
            UUID id,
            RegistroImportacaoDTO registro,
            List<RegistroImportacaoDTO> fontes,
            boolean reprocessar) {
        if (registro.status().equals("IMPORTADO") || registro.status().equals("REJEITADO")) {
            return registro;
        }
        if (reprocessar && !pendenciaPessoal(registro.motivo())) {
            return registro;
        }
        return switch (registro.tabela()) {
            case "curso" -> catalogos.curso(registro);
            case "materia" -> catalogos.materia(registro);
            case "turmas" -> turmas.executar(registro, fontes);
            case "grades" -> grades.executar(registro, fontes);
            case "gradecurricular" -> ofertas.executar(id, registro, fontes);
            case "livros", "evento" -> importado(registro, Map.of());
            case "user" -> pessoa(id, registro);
            case "emprestimolivros" -> participacao(id, registro, fontes, "livros", "idLivro");
            case "participarevento" -> participacao(id, registro, fontes, "evento", "idEvento");
            default -> pendente(registro, "SEM_EQUIVALENCIA_COMPROVADA");
        };
    }

    private RegistroImportacaoDTO pessoa(UUID id, RegistroImportacaoDTO registro) {
        Long pessoaId = origens.pessoaVinculada(id, registro.origemId());
        return pessoaId == null
                ? pendente(registro, "PERFIL_NAO_MAPEADO")
                : importado(registro, Map.of("pessoaId", pessoaId));
    }

    private RegistroImportacaoDTO participacao(
            UUID id,
            RegistroImportacaoDTO registro,
            List<RegistroImportacaoDTO> fontes,
            String tabela,
            String campo) {
        String referencia = registro.dados().get(campo);
        var referidos =
                fontes.stream()
                        .filter(
                                fonte ->
                                        fonte.tabela().equals(tabela)
                                                && referencia != null
                                                && referencia.equals(fonte.origemId())
                                                && fonte.status().equals("IMPORTADO"))
                        .toList();
        if (referidos.size() != 1) {
            return pendente(registro, "REFERENCIA_PRIVADA_NAO_RESOLVIDA");
        }
        Long pessoaId = origens.pessoaVinculada(id, registro.dados().get("idUser"));
        if (pessoaId == null) {
            return pendente(registro, "PESSOA_NAO_VINCULADA");
        }
        var destinos =
                Map.of(
                        "pessoaId",
                        pessoaId,
                        "referenciaPrivadaOrdinal",
                        (long) referidos.getFirst().ordinal());
        if (registro.tabela().equals("emprestimolivros")) {
            // A referência pessoal não define o significado do estado histórico do empréstimo.
            return new RegistroImportacaoDTO(
                    registro.tabela(),
                    registro.ordinal(),
                    registro.origemId(),
                    "PENDENTE",
                    "ESTADO_NAO_MAPEADO",
                    registro.dados(),
                    destinos);
        }
        return importado(registro, destinos);
    }

    private boolean pendenciaPessoal(String motivo) {
        return "PERFIL_NAO_MAPEADO".equals(motivo)
                || "PESSOA_NAO_VINCULADA".equals(motivo)
                || "PROFESSOR_NAO_VINCULADO".equals(motivo);
    }
}
