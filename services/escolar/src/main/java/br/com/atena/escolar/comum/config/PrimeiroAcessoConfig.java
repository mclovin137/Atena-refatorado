package br.com.atena.escolar.comum.config;

import br.com.atena.escolar.pessoa.dao.IPessoaDAO;
import br.com.atena.escolar.pessoa.dominio.Pessoa;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PrimeiroAcessoConfig {
    @Bean
    ApplicationRunner primeiroAcesso(
            IPessoaDAO pessoas, PasswordEncoder encoder, Environment ambiente) {
        return argumentos -> {
            if (!pessoas.listar().isEmpty()) return;
            String email = ambiente.getProperty("ADMIN_EMAIL"),
                    senha = ambiente.getProperty("ADMIN_PASSWORD");
            if (email == null || senha == null || senha.length() < 12)
                throw new IllegalStateException(
                        "Defina ADMIN_EMAIL e ADMIN_PASSWORD de pelo menos 12 caracteres para o"
                                + " primeiro acesso");
            pessoas.salvar(
                    new Pessoa(
                            null,
                            "Secretaria",
                            email,
                            "ADMIN",
                            "SECRETARIA",
                            encoder.encode(senha),
                            true,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null));
        };
    }
}
