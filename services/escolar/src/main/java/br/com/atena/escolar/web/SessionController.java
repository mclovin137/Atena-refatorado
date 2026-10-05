package br.com.atena.escolar.web;
import br.com.atena.escolar.infra.*;
import jakarta.servlet.http.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/session")
public class SessionController {
 private final AuthenticationManager manager; private final PessoaRepository people;private final org.springframework.security.crypto.password.PasswordEncoder encoder;
 public SessionController(AuthenticationManager manager,PessoaRepository people,org.springframework.security.crypto.password.PasswordEncoder encoder){this.manager=manager;this.people=people;this.encoder=encoder;}
 public record Login(@NotBlank @Email String email,@NotBlank String senha){}
 @GetMapping public Map<String,Object> session(HttpServletRequest request, Authentication authentication) {
  CsrfToken token=(CsrfToken)request.getAttribute(CsrfToken.class.getName());
  Map<String,Object> result=new LinkedHashMap<>();result.put("csrfToken",token.getToken());
  if(authentication!=null && authentication.isAuthenticated()) result.put("user",people.findByEmail(authentication.getName()).orElseThrow());
  return result;
 }
 @PostMapping("/login") public Pessoa login(@Valid @RequestBody Login login,HttpServletRequest request,HttpServletResponse response) {
  Authentication auth=manager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(login.email(),login.senha()));
  if(request.getSession(false)!=null)request.changeSessionId();
  SecurityContext context=SecurityContextHolder.createEmptyContext();context.setAuthentication(auth);SecurityContextHolder.setContext(context);
  new HttpSessionSecurityContextRepository().saveContext(context,request,response);
  return people.findByEmail(auth.getName()).orElseThrow();
 }
 @PostMapping("/logout") public void logout(HttpServletRequest request) { if(request.getSession(false)!=null) request.getSession(false).invalidate();SecurityContextHolder.clearContext(); }
 public record Profile(@Size(max=30) String telefone,@Size(max=500) String endereco,@Size(max=12)String cep,@Size(max=150)String pai,@Size(max=150)String mae,@Size(max=40)String estadoCivil,@Size(max=30)String sexo,@Size(max=30)String telefoneResponsavel,@Email @Size(max=180)String emailResponsavel,java.time.LocalDate nascimento){}
 @PutMapping("/profile") public Pessoa profile(@Valid @RequestBody Profile profile,Authentication auth){Pessoa p=people.findByEmail(auth.getName()).orElseThrow();p.telefone=profile.telefone();p.endereco=profile.endereco();if(profile.cep()!=null)p.cep=profile.cep();if(profile.pai()!=null)p.pai=profile.pai();if(profile.mae()!=null)p.mae=profile.mae();if(profile.estadoCivil()!=null)p.estadoCivil=profile.estadoCivil();if(profile.sexo()!=null)p.sexo=profile.sexo();if(profile.telefoneResponsavel()!=null)p.telefoneResponsavel=profile.telefoneResponsavel();if(profile.emailResponsavel()!=null)p.emailResponsavel=profile.emailResponsavel();if(profile.nascimento()!=null)p.nascimento=profile.nascimento();return people.save(p);}
 public record Password(@NotBlank String atual,@NotBlank @Size(min=12,max=100)String nova){}
 @PutMapping("/password") public void password(@Valid @RequestBody Password input,Authentication auth,HttpServletRequest request){Pessoa p=people.findByEmail(auth.getName()).orElseThrow();if(!encoder.matches(input.atual(),p.senhaHash))throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED,"Senha atual inválida");p.senhaHash=encoder.encode(input.nova());people.save(p);if(request.getSession(false)!=null)request.getSession(false).invalidate();SecurityContextHolder.clearContext();}
}
