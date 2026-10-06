package br.com.atena.escolar.importacao.dao.jpa;

import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.importacao.dao.IFonteImportacaoDAO;
import br.com.atena.escolar.importacao.dominio.EstadoImportacaoEnum;
import br.com.atena.escolar.importacao.dominio.FonteCriptografada;
import br.com.atena.escolar.importacao.dto.ContagemImportacaoDTO;
import br.com.atena.escolar.importacao.dto.ImportacaoRespostaDTO;
import br.com.atena.escolar.importacao.dto.MotivoImportacaoDTO;
import br.com.atena.escolar.importacao.dto.TabelaImportacaoDTO;
import br.com.atena.escolar.importacao.service.LerDumpLegadoService;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;

@Repository
public class FonteImportacaoDAO implements IFonteImportacaoDAO {
    private final IImportacaoFonteRepositorioJpa fontes;
    private final JdbcTemplate jdbc;

    public FonteImportacaoDAO(IImportacaoFonteRepositorioJpa fontes, JdbcTemplate jdbc) {
        this.fontes = fontes;
        this.jdbc = jdbc;
    }

    @Override
    public Optional<UUID> buscarDigest(String digest) {
        return fontes.identificarDigest(digest);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public UUID guardar(FonteCriptografada fonte) {
        var inseridas =
                jdbc.query(
                        """
                        INSERT INTO importacao_fonte
                        (id,digest,versao,nonce,conteudo,status,operacao,criado_em,atualizado_em)
                        VALUES (?,?,?,?,?,'ANALISANDO','analisarLegado',now(),now())
                        ON CONFLICT (digest) DO NOTHING RETURNING id
                        """,
                        (linha, indice) -> linha.getObject("id", UUID.class),
                        fonte.identificador(),
                        fonte.digest(),
                        fonte.versao(),
                        fonte.nonce(),
                        fonte.conteudo());
        return inseridas.isEmpty()
                ? buscarDigest(fonte.digest()).orElseThrow()
                : inseridas.getFirst();
    }

    @Override
    public FonteCriptografada fonte(UUID id) {
        var entidade = buscar(id);
        return new FonteCriptografada(
                entidade.versao, entidade.id, entidade.digest, entidade.nonce, entidade.conteudo);
    }

    @Override
    public String operacao(UUID id) {
        var operacoes =
                jdbc.queryForList(
                        "SELECT operacao FROM importacao_fonte WHERE id=?", String.class, id);
        if (operacoes.isEmpty()) {
            throw ErroAplicacao.ausente("Importação não encontrada");
        }
        return operacoes.getFirst();
    }

    @Override
    public ImportacaoRespostaDTO consultar(UUID id) {
        var contagens =
                jdbc.query(
                        """
                        SELECT tabela, status, motivo, count(*) AS quantidade
                        FROM importacao_registro WHERE importacao_id = ?
                        GROUP BY tabela, status, motivo ORDER BY tabela, status, motivo
                        """,
                        (linha, indice) ->
                                new ContagemImportacaoDTO(
                                        linha.getString("tabela"),
                                        linha.getString("status"),
                                        linha.getString("motivo"),
                                        linha.getLong("quantidade")),
                        id);
        var metadados =
                jdbc.query(
                        """
SELECT id,digest,status,execucao_id,criado_em FROM importacao_fonte WHERE id=?
""",
                        (linha, indice) ->
                                new ImportacaoRespostaDTO(
                                        id,
                                        linha.getString("digest"),
                                        EstadoImportacaoEnum.valueOf(linha.getString("status")),
                                        linha.getObject("execucao_id", Long.class),
                                        linha.getTimestamp("criado_em").toInstant(),
                                        contagens,
                                        tabelas(contagens),
                                        motivos(contagens)),
                        id);
        if (metadados.isEmpty()) {
            throw ErroAplicacao.ausente("Importação não encontrada");
        }
        return metadados.getFirst();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void iniciar(UUID id, String operacao, EstadoImportacaoEnum esperado) {
        var estados =
                jdbc.queryForList(
                        "SELECT status FROM importacao_fonte WHERE id=? FOR UPDATE",
                        String.class,
                        id);
        if (estados.isEmpty()) {
            throw ErroAplicacao.ausente("Importação não encontrada");
        }
        if (!estados.getFirst().equals(esperado.name())) {
            throw ErroAplicacao.conflito("Importação não permite esta operação no estado atual");
        }
        var estado =
                operacao.equals("analisarLegado")
                        ? EstadoImportacaoEnum.ANALISANDO
                        : operacao.equals("reprocessarLegado")
                                ? EstadoImportacaoEnum.REPROCESSANDO
                                : EstadoImportacaoEnum.APLICANDO;
        jdbc.update(
                """
                UPDATE importacao_fonte SET operacao=?,status=?,atualizado_em=now() WHERE id=?
                """,
                operacao,
                estado.name(),
                id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void execucao(UUID id, Long execucaoId) {
        if (jdbc.update(
                        """
                        UPDATE importacao_fonte SET execucao_id=?,atualizado_em=now() WHERE id=?
                        """,
                        execucaoId,
                        id)
                == 0) {
            throw ErroAplicacao.ausente("Importação não encontrada");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void finalizar(UUID id, EstadoImportacaoEnum estado) {
        if (jdbc.update(
                        "UPDATE importacao_fonte SET status=?,atualizado_em=now() WHERE id=?",
                        estado.name(),
                        id)
                == 0) {
            throw ErroAplicacao.ausente("Importação não encontrada");
        }
    }

    private ImportacaoFonteEntidadeJpa buscar(UUID id) {
        return fontes.findById(id)
                .orElseThrow(() -> ErroAplicacao.ausente("Importação não encontrada"));
    }

    private List<TabelaImportacaoDTO> tabelas(List<ContagemImportacaoDTO> contagens) {
        return LerDumpLegadoService.TABELAS.stream()
                .sorted()
                .map(
                        nome -> {
                            long lidos = 0, importados = 0, pendentes = 0, rejeitados = 0;
                            for (var contagem : contagens) {
                                if (!nome.equals(contagem.tabela())) continue;
                                lidos += contagem.quantidade();
                                switch (contagem.status()) {
                                    case "IMPORTADO" -> importados += contagem.quantidade();
                                    case "REJEITADO" -> rejeitados += contagem.quantidade();
                                    default -> pendentes += contagem.quantidade();
                                }
                            }
                            return new TabelaImportacaoDTO(
                                    nome, lidos, importados, pendentes, rejeitados);
                        })
                .toList();
    }

    private List<MotivoImportacaoDTO> motivos(List<ContagemImportacaoDTO> contagens) {
        var motivos = new TreeMap<String, Long>();
        for (var contagem : contagens) {
            if (contagem.motivo() != null) {
                motivos.merge(contagem.motivo(), contagem.quantidade(), Long::sum);
            }
        }
        return motivos.entrySet().stream()
                .map(motivo -> new MotivoImportacaoDTO(motivo.getKey(), motivo.getValue()))
                .toList();
    }
}
