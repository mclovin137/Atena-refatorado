package br.com.atena.escolar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Map;
import java.util.UUID;

@SpringBootTest(
        properties = {
            "spring.datasource.url=jdbc:postgresql://localhost:${ATENA_SCHOOL_TEST_PORT:15432}/escolar"
        })
@AutoConfigureMockMvc
class ModeloAcademicoIntegracaoTeste {
    private static final String senha = CredenciaisIntegracao.SENHA;
    private static final String tokenServico = UUID.randomUUID().toString() + UUID.randomUUID();
    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;
    @Autowired private org.springframework.jdbc.core.JdbcTemplate jdbc;

    @DynamicPropertySource
    static void propriedades(DynamicPropertyRegistry propriedades) {
        propriedades.add("ADMIN_EMAIL", () -> CredenciaisIntegracao.EMAIL);
        propriedades.add("ADMIN_PASSWORD", () -> senha);
        propriedades.add("atena.service-token", () -> tokenServico);
    }

    @Test
    void metadadosAcademicosPermitemLeituraMasNegamMutacaoDocenteESemCsrf() throws Exception {
        var secretaria = entrar();
        String email = UUID.randomUUID() + "@atena.test";
        var pessoa =
                enviar(
                        post("/api/pessoas"),
                        Map.of(
                                "pessoa",
                                Map.of(
                                        "nome",
                                        "Docente sintético",
                                        "email",
                                        email,
                                        "matricula",
                                        UUID.randomUUID().toString(),
                                        "perfil",
                                        "PROFESSOR"),
                                "senha",
                                senha),
                        secretaria,
                        201);
        try {
            var professor =
                    (MockHttpSession)
                            mvc.perform(
                                            post("/api/session/login")
                                                    .with(csrf())
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .content(
                                                            mapper.writeValueAsString(
                                                                    Map.of(
                                                                            "email", email, "senha",
                                                                            senha))))
                                    .andExpect(status().isOk())
                                    .andReturn()
                                    .getRequest()
                                    .getSession();
            for (String recurso :
                    new String[] {"faculdades", "grades-curriculares", "requisitos-formacao"}) {
                enviar(get("/api/" + recurso), null, professor, 200);
                Map<String, Object> corpo =
                        switch (recurso) {
                            case "grades-curriculares" -> Map.of("nome", "Restrito", "cursoId", 1L);
                            case "requisitos-formacao" -> Map.of("nome", "Restrito", "gradeId", 1L);
                            default -> Map.of("nome", "Restrito");
                        };
                enviar(post("/api/" + recurso), corpo, professor, 403);
                mvc.perform(
                                post("/api/" + recurso)
                                        .session(secretaria)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"nome\":\"Sem CSRF\"}"))
                        .andExpect(status().isForbidden());
            }
        } finally {
            enviar(delete("/api/pessoas/" + id(pessoa)), null, secretaria, 204);
        }
    }

    @Test
    void cadastroCompletoPreservaContratoLegadoEIntegridade() throws Exception {
        MockHttpSession secretaria = entrar();
        JsonNode faculdade =
                enviar(
                        post("/api/faculdades"),
                        Map.of("nome", "Faculdade " + UUID.randomUUID()),
                        secretaria,
                        201);
        JsonNode cursoLegado =
                enviar(
                        post("/api/cursos"),
                        Map.of("nome", "Curso legado " + UUID.randomUUID()),
                        secretaria,
                        201);
        assertTrue(cursoLegado.get("vinculoInstitucionalPendente").asBoolean());
        JsonNode curso =
                enviar(
                        post("/api/cursos"),
                        Map.of("nome", "Curso " + UUID.randomUUID(), "faculdadeId", id(faculdade)),
                        secretaria,
                        201);
        JsonNode grade =
                enviar(
                        post("/api/grades-curriculares"),
                        Map.of("nome", "Grade " + UUID.randomUUID(), "cursoId", id(curso)),
                        secretaria,
                        201);
        JsonNode requisito =
                enviar(
                        post("/api/requisitos-formacao"),
                        Map.of("nome", "Formação administrativa", "gradeId", id(grade)),
                        secretaria,
                        201);
        JsonNode materia =
                enviar(
                        post("/api/materias"),
                        Map.of("nome", "Matéria " + UUID.randomUUID()),
                        secretaria,
                        201);
        enviar(
                post("/api/requisitos-formacao/" + id(requisito) + "/materias"),
                Map.of("materiaId", id(materia)),
                secretaria,
                201);
        enviar(
                post("/api/requisitos-formacao/" + id(requisito) + "/materias"),
                Map.of("materiaId", id(materia)),
                secretaria,
                409);
        JsonNode associadas =
                enviar(
                        get("/api/requisitos-formacao/" + id(requisito) + "/materias"),
                        null,
                        secretaria,
                        200);
        assertEquals(id(materia), associadas.get(0).get("materiaId").asLong());
        enviar(delete("/api/cursos/" + id(curso)), null, secretaria, 409);
        enviar(delete("/api/faculdades/" + id(faculdade)), null, secretaria, 409);
        enviar(
                post("/api/grades-curriculares"),
                Map.of("nome", "Sem curso", "cursoId", Long.MAX_VALUE),
                secretaria,
                404);
        enviar(
                put("/api/grades-curriculares/" + id(grade)),
                Map.of("id", Long.MAX_VALUE, "nome", "Divergente", "cursoId", id(curso)),
                secretaria,
                400);
        for (String recurso :
                new String[] {"faculdades", "grades-curriculares", "requisitos-formacao"}) {
            mvc.perform(get("/api/" + recurso)).andExpect(status().isUnauthorized());
            enviar(get("/api/" + recurso), null, secretaria, 200);
            enviar(get("/api/" + recurso + "/" + Long.MAX_VALUE), null, secretaria, 404);
            enviar(post("/api/" + recurso), Map.of(), secretaria, 400);
        }
        enviar(
                delete("/api/requisitos-formacao/" + id(requisito) + "/materias/" + id(materia)),
                null,
                secretaria,
                204);
        enviar(delete("/api/requisitos-formacao/" + id(requisito)), null, secretaria, 204);
        enviar(delete("/api/grades-curriculares/" + id(grade)), null, secretaria, 204);
        enviar(delete("/api/materias/" + id(materia)), null, secretaria, 204);
        enviar(delete("/api/cursos/" + id(curso)), null, secretaria, 204);
        enviar(delete("/api/cursos/" + id(cursoLegado)), null, secretaria, 204);
        enviar(delete("/api/faculdades/" + id(faculdade)), null, secretaria, 204);
    }

    private MockHttpSession entrar() throws Exception {
        CredenciaisIntegracao.prepararBootstrap(jdbc);
        return (MockHttpSession)
                mvc.perform(
                                post("/api/session/login")
                                        .with(csrf())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(
                                                mapper.writeValueAsString(
                                                        Map.of(
                                                                "email",
                                                                CredenciaisIntegracao.EMAIL,
                                                                "senha",
                                                                senha))))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getRequest()
                        .getSession();
    }

    private JsonNode enviar(
            MockHttpServletRequestBuilder requisicao,
            Object corpo,
            MockHttpSession sessao,
            int esperado)
            throws Exception {
        requisicao.session(sessao).with(csrf());
        if (corpo != null)
            requisicao
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(corpo));
        byte[] resposta =
                mvc.perform(requisicao)
                        .andExpect(status().is(esperado))
                        .andReturn()
                        .getResponse()
                        .getContentAsByteArray();
        return resposta.length == 0 ? null : mapper.readTree(resposta);
    }

    private long id(JsonNode registro) {
        return registro.get("id").asLong();
    }
}
