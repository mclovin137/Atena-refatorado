package br.com.atena.escolar.web;
import br.com.atena.escolar.infra.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestController
public class InternalListController {
 final PessoaRepository people;final TurmaRepository turmas;final String token;
 public InternalListController(PessoaRepository p,TurmaRepository t,@Value("${atena.service-token}")String token){people=p;turmas=t;this.token=token;}
 @GetMapping("/api/internal/listas/{tipo}") public Map<String,Object> list(@PathVariable String tipo,@RequestHeader(value="X-Service-Token",defaultValue="")String provided){if(!java.security.MessageDigest.isEqual(token.getBytes(java.nio.charset.StandardCharsets.UTF_8),provided.getBytes(java.nio.charset.StandardCharsets.UTF_8)))throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Credencial de serviço inválida");List<Map<String,Object>> lines;
  if(tipo.equals("LISTA_TURMAS"))lines=turmas.findAll().stream().map(t->Map.<String,Object>of("id",t.id,"nome",t.nome,"ano",t.ano)).toList();
  else if(tipo.equals("LISTA_ALUNOS")||tipo.equals("LISTA_PROFESSORES")){String role=tipo.equals("LISTA_ALUNOS")?"ALUNO":"PROFESSOR";lines=people.findAll().stream().filter(p->p.perfil.equals(role)).map(p->Map.<String,Object>of("id",p.id,"nome",p.nome,"matricula",p.matricula)).toList();}
  else throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Lista desconhecida");return Map.of("nome",tipo,"linhas",lines);
 }
}
