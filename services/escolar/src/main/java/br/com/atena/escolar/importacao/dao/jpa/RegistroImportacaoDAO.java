package br.com.atena.escolar.importacao.dao.jpa;

import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.importacao.dao.IRegistroImportacaoDAO;
import br.com.atena.escolar.importacao.dto.RegistroImportacaoDTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class RegistroImportacaoDAO implements IRegistroImportacaoDAO {
    private final JdbcTemplate jdbc;
    private final ObjectMapper json;

    public RegistroImportacaoDAO(JdbcTemplate jdbc, ObjectMapper json) {
        this.jdbc = jdbc;
        this.json = json;
    }

    @Override
    public void guardar(UUID id, RegistroImportacaoDTO registro) {
        jdbc.update(
                """
                INSERT INTO importacao_registro
                (importacao_id,tabela,ordinal,origem_id,status,motivo,dados,destinos)
                VALUES (?,?,?,?,?,?,CAST(? AS jsonb),CAST(? AS jsonb))
                ON CONFLICT (importacao_id,tabela,ordinal) DO NOTHING
                """,
                id,
                registro.tabela(),
                registro.ordinal(),
                registro.origemId(),
                registro.status(),
                registro.motivo(),
                escrever(registro.dados()),
                escrever(registro.destinos()));
    }

    @Override
    public List<RegistroImportacaoDTO> listar(UUID id) {
        return jdbc.query(
                """
                SELECT tabela,ordinal,origem_id,status,motivo,dados::text,destinos::text
                FROM importacao_registro WHERE importacao_id = ? ORDER BY tabela, ordinal
                """,
                (linha, indice) ->
                        new RegistroImportacaoDTO(
                                linha.getString("tabela"),
                                linha.getInt("ordinal"),
                                linha.getString("origem_id"),
                                linha.getString("status"),
                                linha.getString("motivo"),
                                lerDados(linha.getString("dados")),
                                lerDestinos(linha.getString("destinos"))),
                id);
    }

    @Override
    public void resultado(UUID id, RegistroImportacaoDTO registro) {
        jdbc.update(
                """
                UPDATE importacao_registro SET status=?, motivo=?, destinos=CAST(? AS jsonb)
                WHERE importacao_id=? AND tabela=? AND ordinal=?
                """,
                registro.status(),
                registro.motivo(),
                escrever(registro.destinos()),
                id,
                registro.tabela(),
                registro.ordinal());
    }

    @Override
    @Transactional
    public void vincularPessoa(UUID id, String identificadorLegado, Long pessoaId) {
        var estados =
                jdbc.queryForList(
                        "SELECT status FROM importacao_fonte WHERE id=? FOR UPDATE",
                        String.class,
                        id);
        if (estados.isEmpty()) {
            throw ErroAplicacao.ausente("Importação não encontrada");
        }
        if (!estados.getFirst().equals("PLANEJADA")
                && !estados.getFirst().equals("CONCLUIDA_COM_PENDENCIAS")) {
            throw ErroAplicacao.conflito("Importação não permite vínculo no estado atual");
        }
        var fontes =
                jdbc.queryForList(
                        """
SELECT origem_id FROM importacao_registro
WHERE importacao_id=? AND (
    (tabela='user' AND origem_id=?) OR
    (tabela IN ('emprestimolivros','participarevento') AND dados->>'idUser'=?) OR
    (tabela='gradecurricular' AND dados->>'idProf'=?))
""",
                        id,
                        identificadorLegado,
                        identificadorLegado,
                        identificadorLegado);
        if (fontes.isEmpty()) {
            throw ErroAplicacao.ausente("Identificador de pessoa não existe nesta origem");
        }
        var pessoas = jdbc.queryForList("SELECT id FROM pessoa WHERE id=?", pessoaId);
        if (pessoas.isEmpty()) {
            throw ErroAplicacao.ausente("Pessoa não encontrada");
        }
        jdbc.update(
                """
                INSERT INTO importacao_vinculo_pessoa(importacao_id,identificador_legado,pessoa_id)
                VALUES (?,?,?) ON CONFLICT DO NOTHING
                """,
                id,
                identificadorLegado,
                pessoaId);
        if (!pessoaId.equals(pessoaVinculada(id, identificadorLegado))) {
            throw ErroAplicacao.conflito("Identificador já vinculado a outra pessoa");
        }
    }

    @Override
    public Long pessoaVinculada(UUID id, String identificadorLegado) {
        var vinculos =
                jdbc.queryForList(
                        """
                        SELECT pessoa_id FROM importacao_vinculo_pessoa
                        WHERE importacao_id=? AND identificador_legado=?
                        """,
                        Long.class,
                        id,
                        identificadorLegado);
        return vinculos.isEmpty() ? null : vinculos.getFirst();
    }

    private String escrever(Object objeto) {
        try {
            return json.writeValueAsString(objeto);
        } catch (JsonProcessingException erro) {
            throw ErroAplicacao.conflito("Registro privado não pôde ser serializado");
        }
    }

    private Map<String, String> lerDados(String texto) {
        try {
            return json.readValue(texto, new TypeReference<Map<String, String>>() {});
        } catch (JsonProcessingException erro) {
            throw ErroAplicacao.conflito("Registro privado não pôde ser lido");
        }
    }

    private Map<String, Long> lerDestinos(String texto) {
        try {
            return json.readValue(texto, new TypeReference<Map<String, Long>>() {});
        } catch (JsonProcessingException erro) {
            throw ErroAplicacao.conflito("Destino privado não pôde ser lido");
        }
    }
}
