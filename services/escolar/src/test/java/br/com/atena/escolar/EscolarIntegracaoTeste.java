package br.com.atena.escolar;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SpringBootTest(
        properties = {
            "spring.datasource.url=jdbc:postgresql://localhost:${ATENA_SCHOOL_TEST_PORT:15432}/escolar"
        })
@AutoConfigureMockMvc
class EscolarIntegracaoTeste {
    static final String password = CredenciaisIntegracao.SENHA;
    static final String token = UUID.randomUUID().toString() + UUID.randomUUID();
    static final Path arquivosLegados =
            Path.of(System.getProperty("java.io.tmpdir"), "atena-testes-" + UUID.randomUUID());
    static final HttpServer reports = iniciarServidorRelatorios();

    static HttpServer iniciarServidorRelatorios() {
        try {
            var server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
            server.createContext(
                    "/api/relatorios",
                    e -> {
                        String path = e.getRequestURI().getPath();
                        int status = path.endsWith("999") ? 404 : 200;
                        String body =
                                path.endsWith("/download")
                                        ? "%PDF-1.7\nfixture"
                                        : "{\"id\":1,\"ownerId\":1,\"alunoId\":1,\"tipo\":\"HISTORICO\",\"status\":\"COMPLETED\"}";
                        e.getResponseHeaders()
                                .add(
                                        "Content-Type",
                                        path.endsWith("download")
                                                ? "application/pdf"
                                                : "application/json");
                        e.sendResponseHeaders(status, body.length());
                        e.getResponseBody().write(body.getBytes());
                        e.close();
                    });
            server.start();
            return server;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DynamicPropertySource
    static void configurarPropriedades(DynamicPropertyRegistry r) {
        r.add("ADMIN_EMAIL", () -> "secretaria@atena.local");
        r.add("ADMIN_PASSWORD", () -> password);
        r.add("atena.service-token", () -> token);
        r.add("atena.reports-url", () -> "http://127.0.0.1:" + reports.getAddress().getPort());
        r.add("UPLOAD_DIR", arquivosLegados::toString);
    }

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;
    @Autowired org.springframework.jdbc.core.JdbcTemplate jdbc;
    MockHttpSession admin;
    final List<String> created = new ArrayList<>();

    @BeforeEach
    void autenticar() throws Exception {
        CredenciaisIntegracao.prepararBootstrap(jdbc);
        admin = entrarComCookieReal("secretaria@atena.local", password);
    }

    @AfterEach
    void limpar() throws Exception {
        Collections.reverse(created);
        for (String path : created)
            mvc.perform(delete("/api/" + path).session(admin).with(csrf()))
                    .andExpect(status().isNoContent());
    }

    @AfterAll
    static void encerrar() throws Exception {
        reports.stop(0);
        if (Files.exists(arquivosLegados)) {
            try (var arquivos = Files.list(arquivosLegados)) {
                for (Path arquivo : arquivos.toList()) Files.deleteIfExists(arquivo);
            }
            Files.deleteIfExists(arquivosLegados);
        }
    }

    MockHttpSession entrarComCookieReal(String email, String pass) throws Exception {
        var initial = mvc.perform(get("/api/session")).andExpect(status().isOk()).andReturn();
        var cookie = initial.getResponse().getCookie("XSRF-TOKEN");
        assertNotNull(cookie);
        String token =
                mapper.readTree(initial.getResponse().getContentAsByteArray())
                        .get("csrfToken")
                        .asText();
        return (MockHttpSession)
                mvc.perform(
                                post("/api/session/login")
                                        .cookie(cookie)
                                        .header("X-CSRF-TOKEN", token)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(
                                                mapper.writeValueAsString(
                                                        Map.of("email", email, "senha", pass))))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getRequest()
                        .getSession();
    }

    // A primeira autenticação valida o contrato de cookie/header real em contexto sem csrf()
    // anterior.
    // Usuários adicionais usam o suporte de sessão Spring Security para o restante do fluxo de
    // domínio.
    MockHttpSession entrar(String email, String pass) throws Exception {
        return (MockHttpSession)
                mvc.perform(
                                post("/api/session/login")
                                        .with(csrf())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(
                                                mapper.writeValueAsString(
                                                        Map.of("email", email, "senha", pass))))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getRequest()
                        .getSession();
    }

    JsonNode json(MockHttpServletRequestBuilder req, MockHttpSession session, int code)
            throws Exception {
        return mapper.readTree(
                mvc.perform(req.session(session))
                        .andExpect(status().is(code))
                        .andReturn()
                        .getResponse()
                        .getContentAsByteArray());
    }

    JsonNode criar(String path, Map<String, Object> data) throws Exception {
        JsonNode value =
                json(
                        post("/api/" + path)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(data)),
                        admin,
                        201);
        created.add(path + "/" + value.get("id").asLong());
        json(get("/api/" + path), admin, 200);
        json(get("/api/" + path + "/" + value.get("id").asLong()), admin, 200);
        json(
                put("/api/" + path + "/" + value.get("id").asLong())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                mapper.writeValueAsString(
                                        path.equals("pessoas") ? Map.of("pessoa", value) : value)),
                admin,
                200);
        return value;
    }

    JsonNode atualizar(String path, JsonNode body, MockHttpSession session) throws Exception {
        return json(
                put("/api/" + path + "/" + body.get("id").asLong())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)),
                session,
                200);
    }

    long id(JsonNode n) {
        return n.get("id").asLong();
    }

    JsonNode criarPessoa(String prefix, String perfil) throws Exception {
        String email = prefix + UUID.randomUUID() + "@example.test";
        return criar(
                "pessoas",
                Map.of(
                        "pessoa",
                        Map.of(
                                "nome",
                                prefix,
                                "email",
                                email,
                                "matricula",
                                UUID.randomUUID().toString(),
                                "perfil",
                                perfil,
                                "ativo",
                                true,
                                "documento",
                                prefix.equals("Aluno") ? "123.456.789-00" : ""),
                        "senha",
                        password));
    }

    @Test
    void fluxoEscolarCompletoComAutorizacaoLoteEContratos() throws Exception {
        validarSessaoEObservabilidade();
        JsonNode aluno = criarPessoa("Aluno", "ALUNO"),
                outro = criarPessoa("Outro", "ALUNO"),
                professor = criarPessoa("Professor", "PROFESSOR");
        MockHttpSession student = entrar(aluno.get("email").asText(), password),
                teacher = entrar(professor.get("email").asText(), password);
        json(
                put("/api/session/profile")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"telefone\":\"123\",\"endereco\":\"Rua da escola\"}"),
                student,
                200);
        var curso = criar("cursos", Map.of("nome", "Curso " + UUID.randomUUID()));
        var serie = criar("series", Map.of("nome", "Série " + UUID.randomUUID()));
        var materia = criar("materias", Map.of("nome", "Matemática " + UUID.randomUUID()));
        criar(
                "unidades",
                Map.of(
                        "nome",
                        "Escola",
                        "endereco",
                        "Rua principal",
                        "cnpj",
                        "12.345.678/0001-90"));
        var curriculo =
                criar(
                        "curriculos",
                        Map.of(
                                "nome",
                                "Grade teste",
                                "materiaId",
                                id(materia),
                                "cargaHoraria",
                                20));
        var turma =
                criar(
                        "turmas",
                        Map.of(
                                "nome", "Turma A", "cursoId", id(curso), "serieId", id(serie),
                                "ano", 2026));
        JsonNode applied =
                json(
                        post("/api/turmas/" + id(turma) + "/aplicar-curriculo")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"nome\":\"Grade teste\"}"),
                        admin,
                        200);
        assertEquals(1, applied.size());
        long disciplinaId = applied.get(0).get("id").asLong();
        created.add("disciplinas/" + disciplinaId);
        JsonNode disciplina = applied.get(0);
        ((com.fasterxml.jackson.databind.node.ObjectNode) disciplina)
                .put("professorId", id(professor));
        atualizar("disciplinas", disciplina, admin);
        json(get("/api/disciplinas"), admin, 200);
        json(get("/api/disciplinas/" + disciplinaId), admin, 200);
        // Também exercita criação explícita de disciplina, removida antes de preparar notas.
        var materia2 = criar("materias", Map.of("nome", "Outra matéria"));
        var extra =
                criar(
                        "disciplinas",
                        Map.of(
                                "turmaId",
                                id(turma),
                                "materiaId",
                                id(materia2),
                                "professorId",
                                id(professor),
                                "cargaHoraria",
                                20));
        mvc.perform(delete("/api/disciplinas/" + id(extra)).session(admin).with(csrf()))
                .andExpect(status().isNoContent());
        created.remove("disciplinas/" + id(extra));
        var matricula =
                criar(
                        "matriculas",
                        Map.of("turmaId", id(turma), "alunoId", id(aluno), "dependencia", false));
        String associacoes = "/api/matriculas/" + id(matricula) + "/materias";
        mvc.perform(get(associacoes)).andExpect(status().isUnauthorized());
        assertEquals(1, json(get(associacoes), admin, 200).size());
        assertEquals(1, json(get(associacoes), student, 200).size());
        assertEquals(1, json(get(associacoes), teacher, 200).size());
        var alunoAlheio = entrar(outro.get("email").asText(), password);
        mvc.perform(get(associacoes).session(alunoAlheio)).andExpect(status().isForbidden());
        mvc.perform(delete(associacoes + "/" + disciplinaId).session(admin))
                .andExpect(status().isForbidden());
        mvc.perform(delete(associacoes + "/" + disciplinaId).session(student).with(csrf()))
                .andExpect(status().isForbidden());
        mvc.perform(delete(associacoes + "/" + disciplinaId).session(admin).with(csrf()))
                .andExpect(status().isNoContent());
        assertEquals(0, json(get(associacoes), admin, 200).size());
        json(
                post(associacoes)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"disciplinaId\":" + disciplinaId + "}"),
                admin,
                201);
        json(
                post(associacoes)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"disciplinaId\":" + disciplinaId + "}"),
                admin,
                409);
        json(
                post(associacoes)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"disciplinaId\":" + Long.MAX_VALUE + "}"),
                admin,
                404);
        criar(
                "horarios",
                Map.of(
                        "disciplinaId",
                        disciplinaId,
                        "diaSemana",
                        1,
                        "inicio",
                        "08:00:00",
                        "fim",
                        "09:00:00"));
        var aula =
                criar(
                        "aulas",
                        Map.of(
                                "disciplinaId",
                                disciplinaId,
                                "data",
                                "2026-10-04",
                                "conteudo",
                                "Frações",
                                "planejamento",
                                "Exercícios",
                                "horas",
                                2));
        criar("presencas", Map.of("aulaId", id(aula), "alunoId", id(aluno), "faltas", 1));
        var av =
                criar(
                        "avaliacoes",
                        Map.of(
                                "disciplinaId",
                                disciplinaId,
                                "nome",
                                "Atividade 1",
                                "bimestre",
                                1,
                                "valor",
                                100,
                                "data",
                                "2026-10-04",
                                "tipo",
                                "QUALITATIVA"));
        criar("notas-avaliacoes", Map.of("avaliacaoId", id(av), "alunoId", id(aluno), "valor", 20));
        var av2 =
                criar(
                        "avaliacoes",
                        Map.of(
                                "disciplinaId",
                                disciplinaId,
                                "nome",
                                "Atividade 2",
                                "bimestre",
                                1,
                                "valor",
                                100,
                                "data",
                                "2026-10-04",
                                "tipo",
                                "QUALITATIVA"));
        criar(
                "notas-avaliacoes",
                Map.of("avaliacaoId", id(av2), "alunoId", id(aluno), "valor", 30));
        var nota =
                criar(
                        "notas",
                        Map.of(
                                "disciplinaId",
                                disciplinaId,
                                "alunoId",
                                id(aluno),
                                "bimestre",
                                1,
                                "valor",
                                40,
                                "recuperacao",
                                60,
                                "liberada",
                                false,
                                "prazo",
                                "2200-01-01"));
        mvc.perform(get("/api/notas/" + id(nota)).session(student))
                .andExpect(status().isForbidden());
        mvc.perform(get("/api/notas-avaliacoes").session(student))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        String request = UUID.randomUUID().toString();
        JsonNode execution =
                json(
                        post("/api/jobs/inicializar-notas")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        mapper.writeValueAsString(
                                                Map.of(
                                                        "turmaId",
                                                        id(turma),
                                                        "requestId",
                                                        request))),
                        admin,
                        202);
        aguardarJob(execution.get("id").asLong());
        var all = json(get("/api/notas"), admin, 200);
        assertEquals(4, all.size());
        for (JsonNode n : all) {
            if (id(n) != id(nota)) created.add("notas/" + id(n));
            var o = (com.fasterxml.jackson.databind.node.ObjectNode) n;
            o.put("valor", 75);
            o.put("liberada", true);
            o.put("prazo", "2200-01-01");
            atualizar("notas", o, admin);
        }
        JsonNode sum =
                json(
                        post("/api/disciplinas/" + disciplinaId + "/consolidar-avaliacoes")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"bimestre\":1,\"modo\":\"SOMA\"}"),
                        teacher,
                        200);
        assertEquals(50, sum.get(0).get("valor").asDouble());
        JsonNode avg =
                json(
                        post("/api/disciplinas/" + disciplinaId + "/consolidar-avaliacoes")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"bimestre\":1,\"modo\":\"MEDIA\"}"),
                        teacher,
                        200);
        assertEquals(25, avg.get(0).get("valor").asDouble());
        var boletim = json(get("/api/boletins/" + id(aluno)), student, 200);
        assertEquals("Escola", boletim.get("unidade").get("nome").asText());
        assertEquals("12.345.678/0001-90", boletim.get("unidade").get("cnpj").asText());
        assertEquals("123.456.789-00", boletim.get("documento").asText());
        assertEquals(
                "",
                json(get("/api/boletins/" + id(aluno)), teacher, 200).get("documento").asText());
        assertEquals(aluno.get("matricula").asText(), boletim.get("matricula").asText());
        assertEquals(
                "APROVADO", boletim.get("linhas").get(0).get("resultado").get("situacao").asText());
        assertEquals(71.25, boletim.get("linhas").get(0).get("resultado").get("media").asDouble());
        assertEquals(
                95, boletim.get("linhas").get(0).get("resultado").get("frequencia").asDouble());
        var aulaAusencias =
                criar(
                        "aulas",
                        Map.of(
                                "disciplinaId",
                                disciplinaId,
                                "data",
                                "2026-10-05",
                                "conteudo",
                                "Exercícios",
                                "horas",
                                6));
        criar("presencas", Map.of("aulaId", id(aulaAusencias), "alunoId", id(aluno), "faltas", 6));
        assertEquals(
                "REPROVADO_FREQUENCIA",
                json(get("/api/boletins/" + id(aluno)), student, 200)
                        .get("linhas")
                        .get(0)
                        .get("resultado")
                        .get("situacao")
                        .asText());
        validarPrazoEReexecucao(teacher, nota, turma, request);
        criar("eventos", Map.of("titulo", "Reunião", "inicio", "2026-10-04", "fim", "2026-10-04"));
        criar("mensagens", Map.of("destinatarioId", id(aluno), "texto", "Bem-vindo"));
        validarMaterial(student, teacher, aluno, outro, disciplinaId);
        validarIntegracoesEAutorizacao(student, aluno, outro);
        validarPrazoDocente(teacher, nota);
        byte[] avatar = validarAvatar(aluno, student, teacher);
        validarTrocaSenhaELogout(aluno, student, avatar);
    }

    void validarSessaoEObservabilidade() throws Exception {
        json(get("/api/session"), admin, 200);
        mvc.perform(get("/actuator/health")).andExpect(status().isOk());
        json(get("/actuator/info"), admin, 200);
        json(get("/actuator/metrics"), admin, 200);
    }

    void validarPrazoEReexecucao(
            MockHttpSession teacher, JsonNode nota, JsonNode turma, String request)
            throws Exception {
        JsonNode closed = json(get("/api/notas/" + id(nota)), admin, 200);
        ((com.fasterxml.jackson.databind.node.ObjectNode) closed).put("prazo", "1900-01-01");
        atualizar("notas", closed, admin);
        mvc.perform(
                        put("/api/notas/" + id(nota))
                                .session(teacher)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(closed)))
                .andExpect(status().isConflict());
        mvc.perform(
                        post("/api/disciplinas/"
                                        + nota.get("disciplinaId").asLong()
                                        + "/consolidar-avaliacoes")
                                .session(teacher)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"bimestre\":1,\"modo\":\"MEDIA\"}"))
                .andExpect(status().isConflict());
        ((com.fasterxml.jackson.databind.node.ObjectNode) closed).put("prazo", "2200-01-01");
        atualizar("notas", closed, admin);
        // Reexecução com novo request não duplica notas; request concluído retorna409.
        json(
                post("/api/jobs/inicializar-notas")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                mapper.writeValueAsString(
                                        Map.of("turmaId", id(turma), "requestId", request))),
                admin,
                409);
        var rerun =
                json(
                        post("/api/jobs/inicializar-notas")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        mapper.writeValueAsString(
                                                Map.of(
                                                        "turmaId",
                                                        id(turma),
                                                        "requestId",
                                                        UUID.randomUUID().toString()))),
                        admin,
                        202);
        aguardarJob(rerun.get("id").asLong());
        assertEquals(4, json(get("/api/notas"), admin, 200).size());
    }

    void validarMaterial(
            MockHttpSession student,
            MockHttpSession teacher,
            JsonNode aluno,
            JsonNode outro,
            long disciplinaId)
            throws Exception {
        byte[] pdf = "%PDF-1.7\nfixture".getBytes();
        JsonNode material =
                json(
                        multipart("/api/materiais")
                                .file(
                                        new MockMultipartFile(
                                                "file", "aula.pdf", "application/pdf", pdf))
                                .param("disciplinaId", String.valueOf(disciplinaId))
                                .param("nome", "Aula")
                                .with(csrf()),
                        admin,
                        201);
        created.add("materiais/" + id(material));
        validarPersistenciaMaterial(material, pdf, student);
        validarNegativas(student, teacher, aluno, outro, disciplinaId, material);
        json(get("/api/materiais"), student, 200);
        json(get("/api/materiais/" + id(material)), student, 200);
        mvc.perform(get("/api/materiais/" + id(material) + "/download").session(student))
                .andExpect(status().isOk())
                .andExpect(content().bytes(pdf));
        mvc.perform(
                        multipart("/api/materiais")
                                .file(
                                        new MockMultipartFile(
                                                "file", "a.pdf.php", "application/pdf", pdf))
                                .param("disciplinaId", String.valueOf(disciplinaId))
                                .param("nome", "Inválido")
                                .session(admin)
                                .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    void validarIntegracoesEAutorizacao(MockHttpSession student, JsonNode aluno, JsonNode outro)
            throws Exception {
        json(get("/api/catalogos"), admin, 200);
        json(get("/api/directory"), student, 200);
        mvc.perform(get("/api/internal/snapshot/" + id(aluno)))
                .andExpect(status().isUnauthorized());
        json(
                get("/api/internal/snapshot/" + id(aluno)).header("X-Service-Token", token),
                admin,
                200);
        mvc.perform(get("/api/internal/listas/LISTA_ALUNOS")).andExpect(status().isUnauthorized());
        for (String type : List.of("LISTA_ALUNOS", "LISTA_PROFESSORES", "LISTA_TURMAS"))
            json(get("/api/internal/listas/" + type).header("X-Service-Token", token), admin, 200);
        json(
                post("/api/relatorios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                mapper.writeValueAsString(
                                        Map.of(
                                                "alunoId",
                                                id(aluno),
                                                "tipo",
                                                "HISTORICO",
                                                "requestId",
                                                UUID.randomUUID().toString()))),
                admin,
                202);
        json(get("/api/relatorios/1"), admin, 200);
        mvc.perform(get("/api/relatorios/1/download").session(admin)).andExpect(status().isOk());
        json(get("/api/relatorios/999"), admin, 404);
        mvc.perform(get("/api/boletins/" + id(outro)).session(student))
                .andExpect(status().isForbidden());
        mvc.perform(get("/api/pessoas/" + id(outro)).session(student))
                .andExpect(status().isForbidden());
        mvc.perform(
                        post("/api/cursos")
                                .session(student)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"nome\":\"Proibido\"}"))
                .andExpect(status().isForbidden());
        mvc.perform(
                        post("/api/cursos")
                                .session(admin)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"nome\":\"Sem CSRF\"}"))
                .andExpect(status().isForbidden());
        mvc.perform(
                        post("/api/cursos")
                                .session(admin)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"nome\":\"\"}"))
                .andExpect(status().isBadRequest());
        mvc.perform(
                        post("/api/cursos")
                                .session(admin)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"nome\":\"X\",\"desconhecido\":1}"))
                .andExpect(status().isBadRequest());
    }

    void validarPrazoDocente(MockHttpSession teacher, JsonNode nota) throws Exception {
        JsonNode teacherGrade = json(get("/api/notas/" + id(nota)), teacher, 200);
        ((com.fasterxml.jackson.databind.node.ObjectNode) teacherGrade).put("prazo", "1900-01-01");
        mvc.perform(
                        put("/api/notas/" + id(nota))
                                .session(teacher)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(teacherGrade)))
                .andExpect(status().isForbidden());
    }

    byte[] validarAvatar(JsonNode aluno, MockHttpSession student, MockHttpSession teacher)
            throws Exception {
        java.awt.image.BufferedImage image =
                new java.awt.image.BufferedImage(1, 1, java.awt.image.BufferedImage.TYPE_INT_RGB);
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        javax.imageio.ImageIO.write(image, "png", out);
        mvc.perform(
                        multipart("/api/pessoas/" + id(aluno) + "/avatar")
                                .file(
                                        new MockMultipartFile(
                                                "file", "foto.png", "image/png", out.toByteArray()))
                                .session(student)
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.avatarPresente").value(true));
        mvc.perform(get("/api/pessoas/" + id(aluno) + "/avatar").session(student))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG));
        mvc.perform(get("/api/pessoas/" + id(aluno) + "/avatar").session(teacher))
                .andExpect(status().isForbidden());
        mvc.perform(
                        multipart("/api/pessoas/" + id(aluno) + "/avatar")
                                .file(
                                        new MockMultipartFile(
                                                "file",
                                                "foto.png",
                                                "image/png",
                                                "invalid".getBytes()))
                                .session(student)
                                .with(csrf()))
                .andExpect(status().isBadRequest());
        byte[] avatar = validarPersistenciaAvatar(id(aluno), student);
        json(
                put("/api/session/profile")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"telefone\":\"456\",\"endereco\":\"Rua nova\"}"),
                student,
                200);
        assertArrayEquals(
                avatar,
                jdbc.queryForObject(
                        "SELECT avatar_conteudo FROM pessoa WHERE id = ?",
                        byte[].class,
                        id(aluno)));
        return avatar;
    }

    void validarTrocaSenhaELogout(JsonNode aluno, MockHttpSession student, byte[] avatar)
            throws Exception {
        String newPassword = UUID.randomUUID() + "NovaSenha";
        mvc.perform(
                        put("/api/session/password")
                                .session(student)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        mapper.writeValueAsString(
                                                Map.of("atual", password, "nova", newPassword))))
                .andExpect(status().isOk());
        student = entrar(aluno.get("email").asText(), newPassword);
        mvc.perform(get("/api/pessoas/" + id(aluno) + "/avatar").session(student))
                .andExpect(status().isOk())
                .andExpect(content().bytes(avatar));
        mvc.perform(post("/api/session/logout").session(student).with(csrf()))
                .andExpect(status().isOk());
        mvc.perform(get("/api/notas")).andExpect(status().isUnauthorized());
    }

    void validarNegativas(
            MockHttpSession aluno,
            MockHttpSession professor,
            JsonNode pessoa,
            JsonNode outro,
            long disciplinaId,
            JsonNode material)
            throws Exception {
        var recursos =
                List.of(
                        "pessoas",
                        "cursos",
                        "series",
                        "materias",
                        "unidades",
                        "curriculos",
                        "turmas",
                        "disciplinas",
                        "matriculas",
                        "horarios",
                        "aulas",
                        "presencas",
                        "avaliacoes",
                        "notas-avaliacoes",
                        "notas",
                        "eventos",
                        "mensagens");
        for (String recurso : recursos) {
            mvc.perform(get("/api/" + recurso)).andExpect(status().isUnauthorized());
            mvc.perform(get("/api/" + recurso + "/9223372036854775807").session(admin))
                    .andExpect(status().isNotFound());
            mvc.perform(
                            post("/api/" + recurso)
                                    .session(admin)
                                    .with(csrf())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{}"))
                    .andExpect(status().isBadRequest());
        }
        for (String recurso : List.of("cursos", "series", "materias", "unidades")) {
            for (MockHttpSession sessao : List.of(aluno, professor)) {
                mvc.perform(
                                post("/api/" + recurso)
                                        .session(sessao)
                                        .with(csrf())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"nome\":\"Proibido\"}"))
                        .andExpect(status().isForbidden());
                String caminhoCriado =
                        created.stream()
                                .filter(caminho -> caminho.startsWith(recurso + "/"))
                                .findFirst()
                                .orElseThrow();
                JsonNode registro = json(get("/api/" + caminhoCriado), admin, 200);
                mvc.perform(
                                put("/api/" + recurso + "/" + id(registro))
                                        .session(sessao)
                                        .with(csrf())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(mapper.writeValueAsString(registro)))
                        .andExpect(status().isForbidden());
                mvc.perform(
                                delete("/api/" + recurso + "/" + id(registro))
                                        .session(sessao)
                                        .with(csrf()))
                        .andExpect(status().isForbidden());
            }
        }
        for (MockHttpSession sessao : List.of(aluno, professor)) {
            mvc.perform(
                            post("/api/jobs/inicializar-notas")
                                    .session(sessao)
                                    .with(csrf())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(
                                            mapper.writeValueAsString(
                                                    Map.of(
                                                            "turmaId",
                                                            1,
                                                            "requestId",
                                                            UUID.randomUUID().toString()))))
                    .andExpect(status().isForbidden());
        }
        MockHttpSession outroAluno = entrar(outro.get("email").asText(), password);
        mvc.perform(get("/api/disciplinas/" + disciplinaId).session(outroAluno))
                .andExpect(status().isForbidden());
        mvc.perform(get("/api/materiais/" + id(material) + "/download").session(outroAluno))
                .andExpect(status().isForbidden());
        mvc.perform(delete("/api/materiais/" + id(material)).session(aluno).with(csrf()))
                .andExpect(status().isForbidden());
        mvc.perform(
                        get("/api/internal/snapshot/" + id(pessoa))
                                .header("X-Service-Token", "token-incorreto"))
                .andExpect(status().isUnauthorized());
        mvc.perform(get("/api/relatorios/1").session(aluno)).andExpect(status().isForbidden());
        mvc.perform(get("/api/relatorios/1/download").session(aluno))
                .andExpect(status().isForbidden());
    }

    void validarPersistenciaMaterial(JsonNode material, byte[] bytes, MockHttpSession aluno)
            throws Exception {
        assertFalse(material.has("arquivo"));
        assertFalse(material.has("conteudo"));
        long identificador = id(material);
        byte[] copia =
                jdbc.queryForObject(
                        "SELECT conteudo FROM material WHERE id = ?", byte[].class, identificador);
        assertArrayEquals(bytes, copia);
        String arquivo = UUID.randomUUID().toString();
        Files.createDirectories(arquivosLegados);
        Path legado = arquivosLegados.resolve(arquivo);
        Files.write(legado, bytes);
        jdbc.update(
                "UPDATE material SET conteudo = NULL, arquivo = ? WHERE id = ?",
                arquivo,
                identificador);
        mvc.perform(get("/api/materiais/" + identificador + "/download").session(aluno))
                .andExpect(status().isOk())
                .andExpect(content().bytes(bytes));
        assertArrayEquals(
                bytes,
                jdbc.queryForObject(
                        "SELECT conteudo FROM material WHERE id = ?", byte[].class, identificador));
        assertTrue(Files.exists(legado));
        Files.delete(legado);
        mvc.perform(get("/api/materiais/" + identificador + "/download").session(aluno))
                .andExpect(status().isOk())
                .andExpect(content().bytes(bytes));
        jdbc.update("UPDATE material SET conteudo = NULL WHERE id = ?", identificador);
        assertNull(
                jdbc.queryForObject(
                        "SELECT conteudo FROM material WHERE id = ?", byte[].class, identificador));
        jdbc.update("UPDATE material SET conteudo = ? WHERE id = ?", copia, identificador);
        mvc.perform(get("/api/materiais/" + identificador + "/download").session(aluno))
                .andExpect(status().isOk())
                .andExpect(content().bytes(copia));
    }

    byte[] validarPersistenciaAvatar(long pessoaId, MockHttpSession aluno) throws Exception {
        byte[] copia =
                jdbc.queryForObject(
                        "SELECT avatar_conteudo FROM pessoa WHERE id = ?", byte[].class, pessoaId);
        assertNotNull(copia);
        String arquivo = UUID.randomUUID().toString();
        Path legado = arquivosLegados.resolve(arquivo);
        Files.write(legado, copia);
        jdbc.update(
                "UPDATE pessoa SET avatar_conteudo = NULL, avatar = ? WHERE id = ?",
                arquivo,
                pessoaId);
        mvc.perform(get("/api/pessoas/" + pessoaId + "/avatar").session(aluno))
                .andExpect(status().isOk())
                .andExpect(content().bytes(copia));
        assertArrayEquals(
                copia,
                jdbc.queryForObject(
                        "SELECT avatar_conteudo FROM pessoa WHERE id = ?", byte[].class, pessoaId));
        assertTrue(Files.exists(legado));
        Files.delete(legado);
        jdbc.update("UPDATE pessoa SET avatar_conteudo = NULL WHERE id = ?", pessoaId);
        assertNull(
                jdbc.queryForObject(
                        "SELECT avatar_conteudo FROM pessoa WHERE id = ?", byte[].class, pessoaId));
        jdbc.update("UPDATE pessoa SET avatar_conteudo = ? WHERE id = ?", copia, pessoaId);
        mvc.perform(get("/api/pessoas/" + pessoaId + "/avatar").session(aluno))
                .andExpect(status().isOk())
                .andExpect(content().bytes(copia));
        JsonNode resposta = json(get("/api/pessoas/" + pessoaId), aluno, 200);
        assertFalse(resposta.has("avatar"));
        assertFalse(resposta.has("avatarConteudo"));
        assertFalse(resposta.has("senhaHash"));
        return copia;
    }

    void aguardarJob(long id) throws Exception {
        for (int i = 0; i < 100; i++) {
            String state = json(get("/api/jobs/" + id), admin, 200).get("status").asText();
            if (state.equals("COMPLETED")) return;
            if (state.equals("FAILED")) fail("Job falhou");
            Thread.sleep(100);
        }
        fail("Job não concluiu em10s");
    }
}
