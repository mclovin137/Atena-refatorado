package br.com.atena.escolar;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

/** Contextos distintos na mesma base devem autenticar o mesmo bootstrap de testes. */
final class CredenciaisIntegracao {
    static final String EMAIL = "secretaria@atena.local";
    static final String SENHA = UUID.randomUUID() + "-SenhaSegura";

    private CredenciaisIntegracao() {}

    static void prepararBootstrap(JdbcTemplate jdbc) {
        // Somente a fixture de teste é renovada; o bootstrap de produção preserva credenciais.
        jdbc.update(
                "UPDATE pessoa SET senha_hash=?,ativo=true WHERE email=? AND matricula='ADMIN'",
                new BCryptPasswordEncoder().encode(SENHA),
                EMAIL);
    }
}
