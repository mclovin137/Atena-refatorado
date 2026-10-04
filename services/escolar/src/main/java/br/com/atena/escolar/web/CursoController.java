package br.com.atena.escolar.web;
import br.com.atena.escolar.infra.*;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
@RestController @RequestMapping("/api/cursos")
public class CursoController {
 final CursoRepository repository;final Access access;final Invariants invariants;
 public CursoController(CursoRepository repository,Access access,Invariants invariants){this.repository=repository;this.access=access;this.invariants=invariants;}
 @GetMapping public List<Curso> list(Authentication auth){return repository.findAll(org.springframework.data.domain.Sort.by("id")).stream().filter(x->access.readable(x,auth)).toList();}
 @GetMapping("/{id}") public Curso get(@PathVariable Long id,Authentication auth){var value=find(id);access.check(value,auth,false);return value;}
 @org.springframework.cache.annotation.CacheEvict(value="catalogos",allEntries=true) @PostMapping @ResponseStatus(HttpStatus.CREATED) public Curso create(@Valid @RequestBody Curso value,Authentication auth){if(value.id!=null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Não informe id ao criar");access.check(value,auth,true);invariants.validate(value);return repository.save(value);}
 @org.springframework.cache.annotation.CacheEvict(value="catalogos",allEntries=true) @PutMapping("/{id}") public Curso update(@PathVariable Long id,@Valid @RequestBody Curso value,Authentication auth){access.check(find(id),auth,true);value.id=id;access.check(value,auth,true);invariants.validate(value);return repository.save(value);}
 @org.springframework.cache.annotation.CacheEvict(value="catalogos",allEntries=true) @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id,Authentication auth){access.check(find(id),auth,true);repository.deleteById(id);}
 private Curso find(Long id){return repository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Registro não encontrado"));}
}
