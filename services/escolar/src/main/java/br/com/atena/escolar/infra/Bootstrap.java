package br.com.atena.escolar.infra;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
class Bootstrap {
 @Bean ApplicationRunner administrator(PessoaRepository people, PasswordEncoder encoder, org.springframework.core.env.Environment env) {
  return args -> { if(people.count()==0) {
   String email=env.getProperty("ADMIN_EMAIL"), password=env.getProperty("ADMIN_PASSWORD");
   if(email==null || password==null || password.length()<12) throw new IllegalStateException("Defina ADMIN_EMAIL e ADMIN_PASSWORD de pelo menos 12 caracteres para o primeiro acesso");
   Pessoa p=new Pessoa(); p.nome="Secretaria";p.email=email;p.matricula="ADMIN";p.perfil="SECRETARIA";p.senhaHash=encoder.encode(password);p.ativo=true;people.save(p);
  }};
 }
}
