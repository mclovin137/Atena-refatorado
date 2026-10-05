package br.com.atena.escolar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Aplicacao {
    public static void main(String[] args) {
        SpringApplication.run(Aplicacao.class, args);
    }
}
