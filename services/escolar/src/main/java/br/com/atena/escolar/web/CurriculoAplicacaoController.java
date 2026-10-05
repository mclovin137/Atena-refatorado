package br.com.atena.escolar.web;
import br.com.atena.escolar.infra.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
@RestController
public class CurriculoAplicacaoController {
 final CurriculoRepository templates;final DisciplinaRepository disciplines;final TurmaRepository turmas;final Access access;
 public CurriculoAplicacaoController(CurriculoRepository c,DisciplinaRepository d,TurmaRepository t,Access a){templates=c;disciplines=d;turmas=t;access=a;}
 public record Input(@NotBlank String nome){}
 @PostMapping("/api/turmas/{id}/aplicar-curriculo") @Transactional
 public List<Disciplina> apply(@PathVariable Long id,@Valid @RequestBody Input input,Authentication auth){access.admin(auth);if(!turmas.existsById(id))throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,"Turma não encontrada");List<Curriculo> cs=templates.findAll().stream().filter(c->c.nome.equals(input.nome())).toList();if(cs.isEmpty())throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,"Grade não encontrada");for(Curriculo c:cs)if(disciplines.findByTurmaId(id).stream().noneMatch(d->d.materiaId.equals(c.materiaId))){Disciplina d=new Disciplina();d.turmaId=id;d.materiaId=c.materiaId;d.cargaHoraria=c.cargaHoraria;disciplines.save(d);}return disciplines.findByTurmaId(id);}
}
