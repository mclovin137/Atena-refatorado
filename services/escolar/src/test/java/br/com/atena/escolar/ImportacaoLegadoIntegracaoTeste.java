package br.com.atena.escolar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.atena.escolar.importacao.service.CriptografarFonteService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.UUID;

@SpringBootTest(
        properties = {
            "spring.datasource.url=jdbc:postgresql://localhost:${ATENA_SCHOOL_TEST_PORT:15432}/escolar"
        })
@AutoConfigureMockMvc
class ImportacaoLegadoIntegracaoTeste {
    private static final String token = UUID.randomUUID().toString() + UUID.randomUUID();
    private static final String chave = Base64.getEncoder().encodeToString(new byte[32]);
    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper json;
    @Autowired private JdbcTemplate jdbc;

    @DynamicPropertySource
    static void propriedades(DynamicPropertyRegistry propriedades) {
        propriedades.add("ADMIN_EMAIL", () -> CredenciaisIntegracao.EMAIL);
        propriedades.add("ADMIN_PASSWORD", () -> CredenciaisIntegracao.SENHA);
        propriedades.add("atena.service-token", () -> token);
        propriedades.add("IMPORT_ENCRYPTION_KEY", () -> chave);
    }

    @Test
    void aplicacaoAtivaRecusaSegundaExecucaoEReplayNaoDuplica() throws Exception {
        var secretaria = entrar();
        String marcador = UUID.randomUUID().toString();
        String nome = "espera_importacao_" + marcador.replace("-", "");
        String id = null;
        boolean trigger = false;
        try {
            byte[] fonte =
                    ("INSERT INTO `materia` (`id`,`nome`) VALUES (1,'Concorrência "
                                    + marcador
                                    + "');")
                            .getBytes(StandardCharsets.UTF_8);
            id = upload(fonte, secretaria).get("id").asText();
            aguardar(id, secretaria, "PLANEJADA");
            jdbc.execute(
                    "CREATE FUNCTION "
                            + nome
                            + "() RETURNS trigger LANGUAGE plpgsql AS $$ "
                            + "BEGIN IF NEW.nome='Concorrência "
                            + marcador
                            + "' THEN PERFORM pg_sleep(2); END IF; RETURN NEW; END $$");
            jdbc.execute(
                    "CREATE TRIGGER "
                            + nome
                            + " BEFORE INSERT ON materia "
                            + "FOR EACH ROW EXECUTE FUNCTION "
                            + nome
                            + "()");
            trigger = true;
            acao(id, "aplicar", secretaria);
            mvc.perform(
                            post("/api/importacoes/" + id + "/aplicar")
                                    .session(secretaria)
                                    .with(csrf()))
                    .andExpect(status().isConflict());
            mvc.perform(
                            post("/api/importacoes/" + id + "/retomar")
                                    .session(secretaria)
                                    .with(csrf()))
                    .andExpect(status().isConflict());
            aguardar(id, secretaria, "CONCLUIDA");
            acao(id, "aplicar", secretaria);
            assertThat(
                            jdbc.queryForObject(
                                    "SELECT count(*) FROM materia WHERE nome=?",
                                    Integer.class,
                                    "Concorrência " + marcador))
                    .isEqualTo(1);
        } finally {
            if (trigger) jdbc.execute("DROP TRIGGER " + nome + " ON materia");
            jdbc.execute("DROP FUNCTION IF EXISTS " + nome + "()");
            excluirImportacao(id);
            jdbc.update("DELETE FROM materia WHERE nome=?", "Concorrência " + marcador);
        }
    }

    @Test
    void importacaoNegaPerfisCsrfEComandosMaliciososSemExporFonte() throws Exception {
        var secretaria = entrar();
        String id = null;
        try {
            byte[] fonte =
                    "DROP TABLE materia; -- segredo-sintetico-interno"
                            .getBytes(StandardCharsets.UTF_8);
            mvc.perform(
                            multipart("/api/importacoes/analisar")
                                    .file(new MockMultipartFile("file", fonte))
                                    .session(secretaria))
                    .andExpect(status().isForbidden());
            id = upload(fonte, secretaria).get("id").asText();
            var falha = aguardar(id, secretaria, "FAILED");
            assertThat(falha.toString()).doesNotContain("DROP", "segredo-sintetico-interno");
            assertThat(falha.get("tabelas")).hasSize(24);
            assertThat(falha.get("jobId").asLong()).isPositive();
            for (String operacao : new String[] {"aplicar", "reprocessar", "vinculos-pessoas"}) {
                mvc.perform(post("/api/importacoes/" + id + "/" + operacao).session(secretaria))
                        .andExpect(status().isForbidden());
            }
            mvc.perform(
                            post("/api/importacoes/" + id + "/aplicar")
                                    .session(secretaria)
                                    .with(csrf()))
                    .andExpect(status().isConflict());
            for (String perfil : new String[] {"ALUNO", "PROFESSOR"}) {
                String email = UUID.randomUUID() + "@atena.test";
                long pessoa =
                        jdbc.queryForObject(
                                """
                                INSERT INTO pessoa(nome,email,matricula,perfil,ativo)
                                VALUES ('Fixture restrita',?,?,?,true) RETURNING id
                                """,
                                Long.class,
                                email,
                                UUID.randomUUID().toString(),
                                perfil);
                try {
                    mvc.perform(get("/api/importacoes/" + id).with(user(email)))
                            .andExpect(status().isForbidden());
                    mvc.perform(
                                    multipart("/api/importacoes/analisar")
                                            .file(new MockMultipartFile("file", fonte))
                                            .with(user(email))
                                            .with(csrf()))
                            .andExpect(status().isForbidden());
                    for (String operacao :
                            new String[] {
                                "aplicar", "retomar", "reprocessar", "vinculos-pessoas"
                            }) {
                        var pedido =
                                post("/api/importacoes/" + id + "/" + operacao)
                                        .with(user(email))
                                        .with(csrf());
                        if (operacao.equals("vinculos-pessoas")) {
                            pedido.contentType(MediaType.APPLICATION_JSON)
                                    .content(
                                            "{\"identificadorLegado\":\"1\",\"pessoaId\":"
                                                    + pessoa
                                                    + "}");
                        }
                        mvc.perform(pedido).andExpect(status().isForbidden());
                    }
                } finally {
                    jdbc.update("DELETE FROM pessoa WHERE id=?", pessoa);
                }
            }
        } finally {
            excluirImportacao(id);
        }
    }

    @Test
    void planoAplicaSemAtivarContasEResolveSomenteReferenciasExplicitas() throws Exception {
        String marcador = UUID.randomUUID().toString();
        String texto =
                ("""
INSERT INTO `curso` (`id`,`nome`) VALUES (1,'Curso %s');
INSERT INTO `materia` (`id`,`nome`) VALUES (1,'Matéria %s');
INSERT INTO `turmas` (`id`,`idClass`,`ano`,`curso`,`periodo`,`complemento`,`grade`,`estado`,`block`,`senha`)
VALUES (1,'classe-%s',2026,'Curso %s','1° Ano','%s','Grade %s',1,0,'segredo-sintetico');
INSERT INTO `grades` (`id`,`nome`,`materia`,`Ch`) VALUES (1,'Grade %s','Matéria %s',40);
INSERT INTO `gradecurricular` (`id`,`idTurma`,`grade`,`idProf`,`Ch`,`materia`,`estado`)
VALUES (1,'classe-%s','Grade %s','',40,'Matéria %s',1);
INSERT INTO `user` (`id`,`cargo`,`nome`,`email`,`senha`,`hast`)
VALUES (900,4,'Pessoa histórica sintética','historico@atena.test','hash-sintetico','hash-auxiliar');
INSERT INTO `livros` (`id`,`nome`,`autor`,`lancamento`,`estoque`)
VALUES (1,'Livro sintético','Autor sintético','2020-01-01',2);
INSERT INTO `emprestimolivros` (`id`,`idUser`,`idLivro`,`nomeLivro`,`dataEmprestimo`,`dataValidade`,`estado`)
VALUES (1,901,1,'Livro sintético','2020-01-01','2020-01-10',1);
INSERT INTO `evento` (`id`,`nome`,`data`,`local`,`vagas`,`duracao`)
VALUES (1,'Evento sintético','2020-01-01','Local sintético',10,2);
INSERT INTO `participarevento` (`id`,`idUser`,`idEvento`) VALUES (1,902,1);
""")
                        .formatted(
                                marcador, marcador, marcador, marcador, marcador, marcador,
                                marcador, marcador, marcador, marcador, marcador);
        texto +=
                ("""
                INSERT INTO `turmas` (`id`,`idClass`,`ano`,`curso`,`periodo`,`complemento`,`grade`)
                VALUES (2,'historica-%s',2026,'Curso %s','1° Ano','H %s','Grade histórica %s');
                INSERT INTO `grades` (`id`,`nome`,`materia`,`Ch`)
                VALUES (2,'Grade histórica %s','Matéria %s',40);
                """)
                        .formatted(marcador, marcador, marcador, marcador, marcador, marcador);
        byte[] fonte = texto.getBytes(StandardCharsets.UTF_8);
        MockHttpSession secretaria = entrar();
        String id = null;
        long pessoaId = 0;
        try {
            long materiaLegada =
                    jdbc.queryForObject(
                            "INSERT INTO materia(nome) VALUES (?) RETURNING id",
                            Long.class,
                            "Matéria " + marcador);
            jdbc.update(
                    "INSERT INTO curriculo(nome,materia_id,carga_horaria) VALUES (?,?,30)",
                    "Grade histórica " + marcador,
                    materiaLegada);
            mvc.perform(
                            multipart("/api/importacoes/analisar")
                                    .file(new MockMultipartFile("file", fonte))
                                    .with(csrf()))
                    .andExpect(status().isUnauthorized());
            id = upload(fonte, secretaria).get("id").asText();
            var plano = aguardar(id, secretaria, "PLANEJADA");
            assertThat(total(plano)).isEqualTo(12);
            assertThat(plano.get("tabelas")).hasSize(24);
            long lidos = 0;
            for (var tabela : plano.get("tabelas")) {
                lidos += tabela.get("lidos").asLong();
                assertThat(tabela.get("lidos").asLong())
                        .isEqualTo(
                                tabela.get("importados").asLong()
                                        + tabela.get("pendentes").asLong()
                                        + tabela.get("rejeitados").asLong());
            }
            assertThat(lidos).isEqualTo(12);
            assertThat(
                            jdbc.queryForObject(
                                    "SELECT count(*) FROM curso WHERE nome=?",
                                    Integer.class,
                                    "Curso " + marcador))
                    .isZero();
            assertThat(
                            jdbc.queryForObject(
                                    """
SELECT count(*) FROM importacao_registro
WHERE importacao_id=CAST(? AS uuid) AND (jsonb_exists(dados,'senha') OR jsonb_exists(dados,'hast'))
""",
                                    Integer.class,
                                    id))
                    .isZero();
            assertThat(upload(fonte, secretaria).get("id").asText()).isEqualTo(id);
            acao(id, "aplicar", secretaria);
            var aplicada = aguardar(id, secretaria, "CONCLUIDA_COM_PENDENCIAS");
            assertThat(total(aplicada)).isEqualTo(12);
            assertThat(
                            jdbc.queryForObject(
                                    "SELECT count(*) FROM pessoa WHERE"
                                            + " email='historico@atena.test'",
                                    Integer.class))
                    .isZero();
            assertThat(
                            jdbc.queryForObject(
                                    "SELECT count(*) FROM curso WHERE nome=?",
                                    Integer.class,
                                    "Curso " + marcador))
                    .isEqualTo(1);
            assertThat(
                            jdbc.queryForObject(
                                    "SELECT count(*) FROM evento WHERE titulo='Evento sintético'",
                                    Integer.class))
                    .isZero();
            pessoaId =
                    jdbc.queryForObject(
                            """
INSERT INTO pessoa(nome,email,matricula,perfil,ativo) VALUES (?,?,?,'ALUNO',false) RETURNING id
""",
                            Long.class,
                            "Pessoa reconciliada",
                            marcador + "@atena.test",
                            marcador);
            vincular(id, "900", pessoaId, secretaria, 200);
            vincular(id, "901", pessoaId, secretaria, 200);
            vincular(id, "902", pessoaId, secretaria, 200);
            vincular(id, "999", pessoaId, secretaria, 404);
            acao(id, "reprocessar", secretaria);
            var concluida = aguardar(id, secretaria, "CONCLUIDA_COM_PENDENCIAS");
            assertThat(
                            jdbc.queryForObject(
                                    "SELECT count(*) FROM grade_curricular WHERE nome=?",
                                    Integer.class,
                                    "Grade histórica " + marcador))
                    .isZero();
            assertThat(
                            jdbc.queryForObject(
                                    "SELECT grade_id FROM curriculo WHERE nome=?",
                                    Long.class,
                                    "Grade histórica " + marcador))
                    .isNull();
            assertThat(
                            jdbc.queryForObject(
                                    "SELECT carga_horaria FROM curriculo WHERE nome=?",
                                    Integer.class,
                                    "Grade histórica " + marcador))
                    .isEqualTo(30);
            assertThat(
                            jdbc.queryForObject(
                                    """
SELECT count(*) FROM importacao_registro WHERE importacao_id=CAST(? AS uuid)
AND status='PENDENTE' AND motivo='COMPONENTE_EXISTENTE_INCOMPATIVEL'
""",
                                    Integer.class,
                                    id))
                    .isEqualTo(1);
            assertThat(total(concluida)).isEqualTo(12);
            assertThat(
                            jdbc.queryForObject(
                                    """
SELECT count(*) FROM importacao_registro WHERE importacao_id=CAST(? AS uuid)
AND tabela='emprestimolivros' AND status='PENDENTE' AND motivo='ESTADO_NAO_MAPEADO'
AND dados->>'estado'='1' AND jsonb_exists(destinos,'pessoaId')
""",
                                    Integer.class,
                                    id))
                    .isEqualTo(1);
            assertThat(
                            jdbc.queryForObject(
                                    """
SELECT count(*) FROM importacao_registro WHERE importacao_id=CAST(? AS uuid)
AND tabela='participarevento' AND status='IMPORTADO'
""",
                                    Integer.class,
                                    id))
                    .isEqualTo(1);
            acao(id, "aplicar", secretaria);
            assertThat(
                            jdbc.queryForObject(
                                    "SELECT count(*) FROM curso WHERE nome=?",
                                    Integer.class,
                                    "Curso " + marcador))
                    .isEqualTo(1);
            assertThat(
                            jdbc.queryForObject(
                                    "SELECT ativo FROM pessoa WHERE id=?", Boolean.class, pessoaId))
                    .isFalse();
            assertThat(
                            jdbc.queryForObject(
                                    "SELECT senha_hash FROM pessoa WHERE id=?",
                                    String.class,
                                    pessoaId))
                    .isNull();
            assertThat(concluida.toString())
                    .doesNotContain("hash-sintetico", "segredo-sintetico", "historico@");
        } finally {
            excluirImportacao(id);
            excluirAcademico(marcador);
            if (pessoaId != 0) jdbc.update("DELETE FROM pessoa WHERE id=?", pessoaId);
        }
    }

    @Test
    void falhaNoSegundoChunkRetomaSnapshotConfirmadoSemDuplicarPrimeiroChunk() throws Exception {
        String marcador = UUID.randomUUID().toString();
        var sql = new StringBuilder("INSERT INTO `materia` (`id`,`nome`) VALUES ");
        for (int indice = 1; indice <= 110; indice++) {
            if (indice > 1) sql.append(',');
            sql.append('(')
                    .append(indice)
                    .append(",'Matéria ")
                    .append(marcador)
                    .append(' ')
                    .append(indice)
                    .append("')");
        }
        sql.append(';');
        byte[] fonte = sql.toString().getBytes(StandardCharsets.UTF_8);
        String digest = CriptografarFonteService.digest(fonte);
        String sufixo = UUID.randomUUID().toString().replace("-", "");
        String nome = "falha_importacao_" + sufixo;
        MockHttpSession secretaria = entrar();
        String id = null;
        boolean triggerCriado = false;
        try {
            // Nome e digest são gerados por este teste; nenhum SQL do upload é executado.
            jdbc.execute(
                    "CREATE FUNCTION "
                            + nome
                            + "() RETURNS trigger LANGUAGE plpgsql AS $$ BEGIN IF NEW.ordinal=101"
                            + " AND NEW.tabela='materia' AND EXISTS (SELECT 1 FROM importacao_fonte"
                            + " WHERE id=NEW.importacao_id AND digest='"
                            + digest
                            + "') THEN RAISE EXCEPTION 'Falha sintética isolada'; END IF; RETURN"
                            + " NEW; END $$");
            jdbc.execute(
                    "CREATE TRIGGER "
                            + nome
                            + " BEFORE INSERT ON importacao_registro "
                            + "FOR EACH ROW EXECUTE FUNCTION "
                            + nome
                            + "()");
            triggerCriado = true;
            id = upload(fonte, secretaria).get("id").asText();
            aguardar(id, secretaria, "FAILED");
            assertThat(
                            jdbc.queryForObject(
                                    """
SELECT count(*) FROM importacao_registro WHERE importacao_id=CAST(? AS uuid)
""",
                                    Integer.class,
                                    id))
                    .isEqualTo(100);
            assertThat(
                            jdbc.queryForObject(
                                    """
SELECT octet_length(conteudo) FROM importacao_fonte WHERE id=CAST(? AS uuid)
""",
                                    Integer.class,
                                    id))
                    .isGreaterThan(fonte.length);
            jdbc.execute("DROP TRIGGER " + nome + " ON importacao_registro");
            triggerCriado = false;
            java.util.Arrays.fill(fonte, (byte) 'x');
            acao(id, "retomar", secretaria);
            assertThat(total(aguardar(id, secretaria, "PLANEJADA"))).isEqualTo(110);
            assertThat(
                            jdbc.queryForObject(
                                    """
                                    SELECT digest FROM importacao_fonte WHERE id=CAST(? AS uuid)
                                    """,
                                    String.class,
                                    id))
                    .isEqualTo(digest);
            acao(id, "aplicar", secretaria);
            assertThat(total(aguardar(id, secretaria, "CONCLUIDA"))).isEqualTo(110);
            assertThat(
                            jdbc.queryForObject(
                                    "SELECT count(*) FROM materia WHERE nome LIKE ?",
                                    Integer.class,
                                    "Matéria " + marcador + " %"))
                    .isEqualTo(110);
        } finally {
            if (triggerCriado) jdbc.execute("DROP TRIGGER " + nome + " ON importacao_registro");
            jdbc.execute("DROP FUNCTION IF EXISTS " + nome + "()");
            excluirImportacao(id);
            jdbc.update("DELETE FROM materia WHERE nome LIKE ?", "Matéria " + marcador + " %");
        }
    }

    private MockHttpSession entrar() throws Exception {
        CredenciaisIntegracao.prepararBootstrap(jdbc);
        return (MockHttpSession)
                mvc.perform(
                                post("/api/session/login")
                                        .with(csrf())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(
                                                json.writeValueAsString(
                                                        java.util.Map.of(
                                                                "email",
                                                                CredenciaisIntegracao.EMAIL,
                                                                "senha",
                                                                CredenciaisIntegracao.SENHA))))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getRequest()
                        .getSession();
    }

    private JsonNode upload(byte[] fonte, MockHttpSession sessao) throws Exception {
        return json.readTree(
                mvc.perform(
                                multipart("/api/importacoes/analisar")
                                        .file(
                                                new MockMultipartFile(
                                                        "file",
                                                        "fixture.sql",
                                                        "application/sql",
                                                        fonte))
                                        .session(sessao)
                                        .with(csrf()))
                        .andExpect(status().isAccepted())
                        .andReturn()
                        .getResponse()
                        .getContentAsByteArray());
    }

    private void acao(String id, String acao, MockHttpSession sessao) throws Exception {
        mvc.perform(post("/api/importacoes/" + id + "/" + acao).session(sessao).with(csrf()))
                .andExpect(status().isAccepted());
    }

    private void vincular(
            String id, String legado, long pessoa, MockHttpSession sessao, int esperado)
            throws Exception {
        mvc.perform(
                        post("/api/importacoes/" + id + "/vinculos-pessoas")
                                .session(sessao)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        json.writeValueAsString(
                                                java.util.Map.of(
                                                        "identificadorLegado",
                                                        legado,
                                                        "pessoaId",
                                                        pessoa))))
                .andExpect(status().is(esperado));
    }

    private JsonNode aguardar(String id, MockHttpSession sessao, String esperado) throws Exception {
        long limite = System.nanoTime() + Duration.ofSeconds(30).toNanos();
        JsonNode resposta = null;
        while (System.nanoTime() < limite) {
            resposta =
                    json.readTree(
                            mvc.perform(get("/api/importacoes/" + id).session(sessao))
                                    .andExpect(status().isOk())
                                    .andReturn()
                                    .getResponse()
                                    .getContentAsByteArray());
            if (resposta.get("status").asText().equals(esperado)) return resposta;
            if (resposta.get("status").asText().equals("FAILED") && !esperado.equals("FAILED"))
                break;
            Thread.sleep(30);
        }
        throw new AssertionError(
                "Importação não atingiu estado "
                        + esperado
                        + ": "
                        + (resposta == null ? "sem resposta" : resposta.get("status")));
    }

    private long total(JsonNode resposta) {
        long total = 0;
        for (var contagem : resposta.get("contagens")) total += contagem.get("quantidade").asLong();
        return total;
    }

    private void excluirImportacao(String id) {
        if (id == null) return;
        jdbc.update(
                "DELETE FROM importacao_vinculo_pessoa WHERE importacao_id=CAST(? AS uuid)", id);
        jdbc.update("DELETE FROM importacao_registro WHERE importacao_id=CAST(? AS uuid)", id);
        jdbc.update("DELETE FROM importacao_fonte WHERE id=CAST(? AS uuid)", id);
    }

    private void excluirAcademico(String marcador) {
        jdbc.update(
                "DELETE FROM disciplina WHERE turma_id IN (SELECT id FROM turma WHERE nome LIKE ?)",
                "%" + marcador);
        jdbc.update("DELETE FROM turma WHERE nome LIKE ?", "%" + marcador);
        jdbc.update(
                "DELETE FROM curriculo WHERE nome IN (?,?)",
                "Grade " + marcador,
                "Grade histórica " + marcador);
        jdbc.update("DELETE FROM grade_curricular WHERE nome=?", "Grade " + marcador);
        jdbc.update("DELETE FROM materia WHERE nome=?", "Matéria " + marcador);
        jdbc.update("DELETE FROM curso WHERE nome=?", "Curso " + marcador);
        // Série é agrupamento compartilhado por literal; não apagar um cadastro de outro fluxo.
    }
}
