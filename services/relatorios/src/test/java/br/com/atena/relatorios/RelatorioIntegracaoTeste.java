package br.com.atena.relatorios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.atena.relatorios.linha.dao.jpa.ILinhaJpaRepository;
import br.com.atena.relatorios.relatorio.dao.jpa.IRelatorioJpaRepository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.text.PDFTextStripper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootTest(
        properties = {
            "spring.datasource.url=jdbc:postgresql://localhost:${ATENA_REPORT_TEST_PORT:15432}/relatorios"
        })
@AutoConfigureMockMvc
class RelatorioIntegracaoTeste {
    private static final String TOKEN = UUID.randomUUID().toString() + UUID.randomUUID();
    private static final Set<Long> FALHAS_ORIGEM = ConcurrentHashMap.newKeySet();
    private static final AtomicLong SEQUENCIA_ALUNOS = new AtomicLong(1000);
    private static final String SNAPSHOT =
            """
{"nome":"José da Silva","alunoId":1,"matricula":"ALU001","documento":"123.456.789-00",
 "unidade":{"nome":"Escola Atena","cnpj":"12.345.678/0001-90","endereco":"Rua da escola","telefone":"000000"},
 "linhas":[{"disciplina":"Matemática","ano":2026,"notas":[60,70,80,90],
            "resultado":{"media":75,"situacao":"APROVADO"}}]}
""";
    private static final Map<Long, String> FONTES = new ConcurrentHashMap<>();
    private static final HttpServer ESCOLA = iniciarEscola();

    @Autowired private JdbcTemplate banco;
    @Autowired private MockMvc http;
    @Autowired private ObjectMapper mapper;
    @Autowired private IRelatorioJpaRepository relatorios;
    @Autowired private ILinhaJpaRepository linhas;
    private final List<Long> identificadores = new ArrayList<>();
    private final List<Long> alunosFixture = new ArrayList<>();

    private static HttpServer iniciarEscola() {
        try {
            HttpServer servidor = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
            servidor.createContext(
                    "/api/internal",
                    requisicao -> {
                        boolean autorizado =
                                TOKEN.equals(
                                        requisicao.getRequestHeaders().getFirst("X-Service-Token"));
                        String caminho = requisicao.getRequestURI().getPath();
                        boolean lista = caminho.contains("/listas/");
                        Long alunoId =
                                lista
                                        ? null
                                        : Long.valueOf(
                                                caminho.substring(caminho.lastIndexOf('/') + 1));
                        int codigo =
                                autorizado
                                        ? (alunoId != null && FALHAS_ORIGEM.contains(alunoId)
                                                ? 503
                                                : 200)
                                        : 401;
                        String resposta =
                                lista ? lista(caminho) : FONTES.getOrDefault(alunoId, SNAPSHOT);
                        byte[] conteudo = resposta.getBytes(StandardCharsets.UTF_8);
                        requisicao.getResponseHeaders().add("Content-Type", "application/json");
                        requisicao.sendResponseHeaders(codigo, conteudo.length);
                        requisicao.getResponseBody().write(conteudo);
                        requisicao.close();
                    });
            servidor.start();
            return servidor;
        } catch (Exception erro) {
            throw new IllegalStateException("Não foi possível iniciar origem de teste", erro);
        }
    }

    private static String lista(String caminho) {
        String tipo = caminho.substring(caminho.lastIndexOf('/') + 1);
        String registro =
                tipo.equals("LISTA_TURMAS")
                        ? "{\"id\":1,\"nome\":\"Turma A\",\"ano\":2026}"
                        : "{\"id\":1,\"nome\":\"Pessoa de teste\",\"matricula\":\"MAT001\"}";
        return "{\"nome\":\"" + tipo + "\",\"linhas\":[" + registro + "]}";
    }

    @DynamicPropertySource
    static void propriedades(DynamicPropertyRegistry propriedades) {
        propriedades.add("atena.service-token", () -> TOKEN);
        propriedades.add(
                "atena.school-url", () -> "http://127.0.0.1:" + ESCOLA.getAddress().getPort());
    }

    @AfterEach
    void limpar() {
        for (Long identificador : identificadores) {
            linhas.deleteAll(linhas.findByRelatorioIdOrderByNumero(identificador));
            relatorios.deleteById(identificador);
        }
        for (Long alunoId : alunosFixture) {
            FALHAS_ORIGEM.remove(alunoId);
            FONTES.remove(alunoId);
        }
    }

    @AfterAll
    static void encerrarOrigem() {
        ESCOLA.stop(0);
    }

    private JsonNode criar(String tipo, String requestId) throws Exception {
        return criar(tipo, requestId, 1L);
    }

    private JsonNode criar(String tipo, String requestId, Long alunoId) throws Exception {
        byte[] entrada =
                mapper.writeValueAsBytes(
                        Map.of(
                                "alunoId",
                                alunoId,
                                "ownerId",
                                1,
                                "tipo",
                                tipo,
                                "requestId",
                                requestId));
        byte[] resposta =
                http.perform(
                                post("/api/relatorios")
                                        .header("X-Service-Token", TOKEN)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(entrada))
                        .andExpect(status().isAccepted())
                        .andReturn()
                        .getResponse()
                        .getContentAsByteArray();
        return mapper.readTree(resposta);
    }

    private JsonNode consultar(long identificador) throws Exception {
        byte[] resposta =
                http.perform(
                                get("/api/relatorios/" + identificador)
                                        .header("X-Service-Token", TOKEN))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsByteArray();
        return mapper.readTree(resposta);
    }

    private void aguardar(long identificador, String esperado) throws Exception {
        long limite = System.nanoTime() + java.time.Duration.ofSeconds(15).toNanos();
        while (System.nanoTime() < limite) {
            String estado = consultar(identificador).get("status").asText();
            if (estado.equals(esperado)) {
                return;
            }
            if (estado.equals("FAILED") && !esperado.equals("FAILED")) {
                fail("Relatório falhou");
            }
            Thread.sleep(100); // Polling com prazo, termina imediatamente quando estado esperado
            // aparece.
        }
        fail("Relatório não atingiu " + esperado);
    }

    @Test
    void consolidarExportarEAutorizar() throws Exception {
        http.perform(get("/actuator/health")).andExpect(status().isOk());
        http.perform(get("/actuator/info").header("X-Service-Token", TOKEN))
                .andExpect(status().isOk());
        http.perform(get("/actuator/metrics").header("X-Service-Token", TOKEN))
                .andExpect(status().isOk());
        http.perform(post("/api/relatorios").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isUnauthorized());
        http.perform(get("/api/relatorios/1")).andExpect(status().isUnauthorized());

        String requestId = UUID.randomUUID().toString();
        long identificador = criar("HISTORICO", requestId).get("id").asLong();
        identificadores.add(identificador);
        aguardar(identificador, "COMPLETED");
        assertEquals(1, linhas.findByRelatorioIdOrderByNumero(identificador).size());
        assertNotNull(relatorios.findById(identificador).orElseThrow().snapshot);
        assertEquals(identificador, criar("HISTORICO", requestId).get("id").asLong());

        byte[] pdf =
                http.perform(
                                get("/api/relatorios/" + identificador + "/download")
                                        .header("X-Service-Token", TOKEN))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                        .andReturn()
                        .getResponse()
                        .getContentAsByteArray();
        try (var documento = Loader.loadPDF(pdf)) {
            String texto = new PDFTextStripper().getText(documento);
            for (String esperado :
                    List.of(
                            "José da Silva",
                            "Matemática",
                            "Escola Atena",
                            "12.345.678/0001-90",
                            "123.456.789-00",
                            "ALU001")) {
                assertTrue(texto.contains(esperado), esperado);
            }
        }
        http.perform(
                        get("/api/relatorios/" + identificador + "/download?format=json")
                                .header("X-Service-Token", TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.linhas[0].resultado.media").value(75));
        http.perform(
                        get("/api/relatorios/" + identificador + "/download?format=csv")
                                .header("X-Service-Token", TOKEN))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("registro;conteudo")));
        http.perform(
                        get("/api/relatorios/" + identificador + "/download?format=xml")
                                .header("X-Service-Token", TOKEN))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Formato inválido"));
        http.perform(get("/api/relatorios/999999").header("X-Service-Token", TOKEN))
                .andExpect(status().isNotFound());

        for (String tipo :
                List.of("BOLETIM", "LISTA_ALUNOS", "LISTA_PROFESSORES", "LISTA_TURMAS")) {
            long proximo = criar(tipo, UUID.randomUUID().toString()).get("id").asLong();
            identificadores.add(proximo);
            aguardar(proximo, "COMPLETED");
            http.perform(
                            get("/api/relatorios/" + proximo + "/download")
                                    .header("X-Service-Token", TOKEN))
                    .andExpect(status().isOk());
            http.perform(
                            get("/api/relatorios/" + proximo + "/download?format=json")
                                    .header("X-Service-Token", TOKEN))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.linhas.length()").value(1));
        }
        http.perform(
                        post("/api/relatorios")
                                .header("X-Service-Token", TOKEN)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"ownerId\":1,\"tipo\":\"INVALIDO\",\"requestId\":\"x\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void snapshotFixoSobreviveFalhaNaGravacao() throws Exception {
        String requestId = UUID.randomUUID().toString();
        String objeto = "atena_test_" + requestId.replace("-", "");
        Long alunoId = SEQUENCIA_ALUNOS.incrementAndGet();
        alunosFixture.add(alunoId);
        banco.execute(
                "CREATE FUNCTION "
                        + objeto
                        + "() RETURNS trigger LANGUAGE plpgsql AS $$ BEGIN IF EXISTS (SELECT 1 FROM"
                        + " relatorio WHERE id=NEW.relatorio_id AND request_id='"
                        + requestId
                        + "') THEN RAISE EXCEPTION 'Falha de teste na escrita'; END IF; RETURN NEW;"
                        + " END $$");
        banco.execute(
                "CREATE TRIGGER "
                        + objeto
                        + " BEFORE INSERT ON linha "
                        + "FOR EACH ROW EXECUTE FUNCTION "
                        + objeto
                        + "()");
        long identificador;
        try {
            identificador = criar("HISTORICO", requestId, alunoId).get("id").asLong();
            identificadores.add(identificador);
            aguardar(identificador, "FAILED");
        } finally {
            banco.execute("DROP TRIGGER " + objeto + " ON linha");
            banco.execute("DROP FUNCTION " + objeto + "()");
        }
        assertNotNull(
                relatorios.findById(identificador).orElseThrow().snapshot,
                "Snapshot autorizado deve persistir antes da escrita das linhas");
        FONTES.put(
                alunoId,
                SNAPSHOT.replace("José da Silva", "Origem alterada")
                        .replace("Matemática", "Outra matéria"));
        assertEquals(identificador, criar("HISTORICO", requestId, alunoId).get("id").asLong());
        aguardar(identificador, "COMPLETED");
        http.perform(
                        get("/api/relatorios/" + identificador + "/download?format=json")
                                .header("X-Service-Token", TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("José da Silva"))
                .andExpect(jsonPath("$.linhas[0].disciplina").value("Matemática"));
        assertEquals(1, linhas.findByRelatorioIdOrderByNumero(identificador).size());
    }

    @Test
    void falhaERetomadaPersistidasSemDuplicar() throws Exception {
        Long alunoId = SEQUENCIA_ALUNOS.incrementAndGet();
        alunosFixture.add(alunoId);
        FALHAS_ORIGEM.add(alunoId);
        String requestId = UUID.randomUUID().toString();
        long identificador = criar("HISTORICO", requestId, alunoId).get("id").asLong();
        identificadores.add(identificador);
        aguardar(identificador, "FAILED");
        http.perform(
                        get("/api/relatorios/" + identificador + "/download")
                                .header("X-Service-Token", TOKEN))
                .andExpect(status().isConflict());
        FALHAS_ORIGEM.remove(alunoId);
        assertEquals(identificador, criar("HISTORICO", requestId, alunoId).get("id").asLong());
        aguardar(identificador, "COMPLETED");
        assertEquals(1, linhas.findByRelatorioIdOrderByNumero(identificador).size());
    }

    @Test
    void boletimFiltraAnoEPreservaNulosEUnidadeVazia() throws Exception {
        Long alunoId = SEQUENCIA_ALUNOS.incrementAndGet();
        alunosFixture.add(alunoId);
        String linha =
                """
                {"turma":"Turma de teste","ano":2026,"disciplina":"Matemática","disciplinaId":1,
                 "cargaHoraria":100,"dependencia":false,"notas":[null,null,null,null],
                 "recuperacoes":[null,null,null,null],"faltas":0,
                 "resultado":{"media":0,"parcial":0,"frequencia":100,"situacao":"EM_ANDAMENTO"}}
                """;
        FONTES.put(
                alunoId,
                "{\"alunoId\":1,\"nome\":\"Aluno de teste\",\"matricula\":\"ALU001\","
                        + "\"documento\":\"\",\"unidade\":{},\"linhas\":["
                        + linha.replace("2026", "2025")
                        + ","
                        + linha
                        + "]}");
        long identificador =
                criar("BOLETIM", UUID.randomUUID().toString(), alunoId).get("id").asLong();
        identificadores.add(identificador);
        aguardar(identificador, "COMPLETED");
        JsonNode snapshot =
                mapper.readTree(relatorios.findById(identificador).orElseThrow().snapshot);
        assertEquals(0, snapshot.get("unidade").size());
        assertEquals("", snapshot.get("documento").asText());
        assertEquals(1, snapshot.get("linhas").size());
        assertEquals(2026, snapshot.get("linhas").get(0).get("ano").asInt());
        http.perform(
                        get("/api/relatorios/" + identificador + "/download?format=json")
                                .header("X-Service-Token", TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.linhas[0].notas.length()").value(4))
                .andExpect(jsonPath("$.linhas[0].notas[0]").value(Matchers.nullValue()))
                .andExpect(jsonPath("$.linhas[0].recuperacoes.length()").value(4))
                .andExpect(jsonPath("$.linhas[0].recuperacoes[3]").value(Matchers.nullValue()));
        byte[] pdf =
                http.perform(
                                get("/api/relatorios/" + identificador + "/download")
                                        .header("X-Service-Token", TOKEN))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsByteArray();
        try (var documento = Loader.loadPDF(pdf)) {
            assertTrue(!new PDFTextStripper().getText(documento).contains("CPF / documento:"));
        }
    }
}
