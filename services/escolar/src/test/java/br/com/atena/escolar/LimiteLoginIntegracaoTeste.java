package br.com.atena.escolar;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import java.util.UUID;

/** Contexto próprio: não modifica o repositório CSRF com post-processadores de teste. */
@SpringBootTest
@AutoConfigureMockMvc
class LimiteLoginIntegracaoTeste {
    private static final String token = UUID.randomUUID().toString() + UUID.randomUUID();

    @DynamicPropertySource
    static void configurar(DynamicPropertyRegistry properties) {
        properties.add("atena.service-token", () -> token);
    }

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    @Test
    void loginLimitaTentativasComCookieCsrfRealMesmoTrocandoSessao() throws Exception {
        for (int n = 0; n < 21; n++) {
            var initial = mvc.perform(get("/api/session")).andExpect(status().isOk()).andReturn();
            var cookie = initial.getResponse().getCookie("XSRF-TOKEN");
            assertNotNull(cookie);
            var csrf =
                    mapper.readTree(initial.getResponse().getContentAsByteArray())
                            .get("csrfToken")
                            .asText();
            var resultado =
                    mvc.perform(
                            post("/api/session/login")
                                    .cookie(cookie)
                                    .header("X-CSRF-TOKEN", csrf)
                                    .with(
                                            request -> {
                                                request.setRemoteAddr("192.0.2.77");
                                                return request;
                                            })
                                    .session(new MockHttpSession())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(
                                            n < 20
                                                    ? "{\"email\":\"ausente@qa.invalid\"}"
                                                    : "{\"email\":\"ausente@qa.invalid\",\"senha\":\"incorreta\"}"));
            if (n < 20) resultado.andExpect(status().isBadRequest());
            else
                assertNotNull(
                        resultado
                                .andExpect(status().isTooManyRequests())
                                .andReturn()
                                .getResponse()
                                .getHeader("Retry-After"));
        }
    }
}
