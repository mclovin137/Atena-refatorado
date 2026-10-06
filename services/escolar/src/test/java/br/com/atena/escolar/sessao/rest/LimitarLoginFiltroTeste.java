package br.com.atena.escolar.sessao.rest;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.concurrent.atomic.AtomicLong;

class LimitarLoginFiltroTeste {
    @Test
    void janelaLimitaMesmoIpApesarNovaSessaoEExpiraSemBloquearOutrasRotas() throws Exception {
        var tempo = new AtomicLong(100);
        var filtro = new LimitarLoginFiltro(tempo::get, 2);
        for (int tentativa = 0; tentativa < 20; tentativa++) {
            assertThat(enviar(filtro, "127.0.0.1", "/api/session/login").getStatus())
                    .isEqualTo(204);
        }
        var limite = enviar(filtro, "127.0.0.1", "/api/session/login");
        assertThat(limite.getStatus()).isEqualTo(429);
        assertThat(limite.getHeader("Retry-After")).isEqualTo("60");
        assertThat(enviar(filtro, "127.0.0.1", "/api/session/logout").getStatus()).isEqualTo(204);
        assertThat(enviar(filtro, "127.0.0.2", "/api/session/login").getStatus()).isEqualTo(204);
        assertThat(enviar(filtro, "127.0.0.3", "/api/session/login").getStatus()).isEqualTo(429);
        tempo.addAndGet(60);
        assertThat(enviar(filtro, "127.0.0.1", "/api/session/login").getStatus()).isEqualTo(204);
    }

    private MockHttpServletResponse enviar(LimitarLoginFiltro filtro, String ip, String rota)
            throws Exception {
        var pedido = new MockHttpServletRequest("POST", rota);
        pedido.setServletPath(rota);
        pedido.setRemoteAddr(ip);
        pedido.addHeader("X-Forwarded-For", "ip-ignoravel");
        pedido.getSession();
        var resposta = new MockHttpServletResponse();
        filtro.doFilter(
                pedido, resposta, (req, res) -> ((MockHttpServletResponse) res).setStatus(204));
        return resposta;
    }
}
