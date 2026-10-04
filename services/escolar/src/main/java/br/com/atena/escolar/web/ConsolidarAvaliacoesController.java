package br.com.atena.escolar.web;
import br.com.atena.escolar.infra.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
@RestController
public class ConsolidarAvaliacoesController {
 final AvaliacaoRepository assessments;final NotaAvaliacaoRepository scores;final NotaRepository grades;final MatriculaRepository enrollments;final DisciplinaRepository disciplines;final Access access;
 public ConsolidarAvaliacoesController(AvaliacaoRepository a,NotaAvaliacaoRepository s,NotaRepository g,MatriculaRepository m,DisciplinaRepository d,Access access){assessments=a;scores=s;grades=g;enrollments=m;disciplines=d;this.access=access;}
 public record Input(@Min(1) @Max(4) int bimestre,@NotBlank @Pattern(regexp="SOMA|MEDIA") String modo){}
 @PostMapping("/api/disciplinas/{id}/consolidar-avaliacoes") @Transactional
 public List<Nota> consolidate(@PathVariable Long id,@Valid @RequestBody Input input,Authentication auth){access.discipline(id,auth,true);Disciplina d=disciplines.findById(id).orElseThrow();List<Avaliacao> av=assessments.findAll().stream().filter(a->a.disciplinaId.equals(id)&&a.bimestre==input.bimestre()&&a.tipo.equals("QUALITATIVA")).toList();if(av.size()<2)throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,"São necessárias ao menos duas avaliações qualitativas");List<Nota> result=new ArrayList<>();for(Matricula m:enrollments.findByTurmaId(d.turmaId)){double sum=0;int count=0;for(Avaliacao a:av){Optional<NotaAvaliacao> score=scores.findByAvaliacaoId(a.id).stream().filter(s->s.alunoId.equals(m.alunoId)).findFirst();if(score.isPresent()){sum+=score.get().valor;count++;}}if(count==0)continue;Nota n=grades.findByDisciplinaIdAndAlunoIdOrderByBimestre(id,m.alunoId).stream().filter(g->g.bimestre==input.bimestre()).findFirst().orElseGet(Nota::new);n.disciplinaId=id;n.alunoId=m.alunoId;n.bimestre=input.bimestre();access.check(n,auth,true);n.valor=Math.min(100,input.modo().equals("MEDIA")?sum/count:sum);result.add(grades.save(n));}return result;}
}
