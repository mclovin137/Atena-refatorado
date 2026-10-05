package br.com.atena.escolar.web;
import br.com.atena.escolar.infra.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/pessoas")
public class PessoaController {
 final PessoaRepository people;final PasswordEncoder encoder;final Access access;
 public PessoaController(PessoaRepository people,PasswordEncoder encoder,Access access){this.people=people;this.encoder=encoder;this.access=access;}
 public record Input(@Valid @NotNull Pessoa pessoa,@Size(min=12,max=100) String senha){}
 @GetMapping public List<Pessoa> list(Authentication a){return people.findAll().stream().filter(p->access.readable(p,a)).toList();}
 @GetMapping("/{id}") public Pessoa get(@PathVariable Long id,Authentication a){Pessoa p=find(id);access.check(p,a,false);return p;}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public Pessoa create(@Valid @RequestBody Input input,Authentication a){access.admin(a);Pessoa p=input.pessoa();if(p.id!=null||input.senha()==null)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Senha obrigatória e id gerado pelo sistema");p.senhaHash=encoder.encode(input.senha());return people.save(p);}
 @PutMapping("/{id}") public Pessoa update(@PathVariable Long id,@Valid @RequestBody Input input,Authentication a){access.admin(a);Pessoa old=find(id),p=input.pessoa();if(id.equals(access.person(a).id)&&(!p.ativo||!p.perfil.equals("SECRETARIA")))throw new ResponseStatusException(HttpStatus.CONFLICT,"Não desative ou remova seu próprio perfil de secretaria");if(!old.perfil.equals(p.perfil))throw new ResponseStatusException(HttpStatus.CONFLICT,"Perfil de uma pessoa não pode mudar após cadastro");if(!old.matricula.equals(p.matricula))throw new ResponseStatusException(HttpStatus.CONFLICT,"Matrícula é imutável");p.avatar=old.avatar;p.id=id;p.senhaHash=input.senha()==null?old.senhaHash:encoder.encode(input.senha());return people.save(p);}
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id,Authentication a){access.admin(a);if(id.equals(access.person(a).id))throw new ResponseStatusException(HttpStatus.CONFLICT,"Não exclua sua própria conta");people.delete(find(id));}
 Pessoa find(Long id){return people.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Pessoa não encontrada"));}
}
