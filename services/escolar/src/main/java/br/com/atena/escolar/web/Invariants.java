package br.com.atena.escolar.web;
import br.com.atena.escolar.infra.*;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
@Component
public class Invariants {
 private final PessoaRepository people; private final DisciplinaRepository disciplines;private final MatriculaRepository enrollments;private final AulaRepository lessons;private final AvaliacaoRepository assessments;
 public Invariants(PessoaRepository p,DisciplinaRepository d,MatriculaRepository m,AulaRepository a,AvaliacaoRepository v){people=p;disciplines=d;enrollments=m;lessons=a;assessments=v;}
 void bad(String reason){throw new ResponseStatusException(HttpStatus.BAD_REQUEST,reason);}
 void role(Long id,String role){Pessoa p=people.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Pessoa não encontrada"));if(!p.perfil.equals(role)||!p.ativo)bad("Perfil incompatível ou pessoa inativa");}
 void enrolled(Long discipline,Long student){role(student,"ALUNO");Disciplina d=disciplines.findById(discipline).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Disciplina não encontrada"));if(!enrollments.existsByTurmaIdAndAlunoId(d.turmaId,student))bad("Aluno não matriculado na turma");}
 public void validate(Object e){
  if(e instanceof Disciplina x && x.professorId!=null)role(x.professorId,"PROFESSOR");
  if(e instanceof Matricula x)role(x.alunoId,"ALUNO");
  if(e instanceof Horario x && !x.fim.isAfter(x.inicio))bad("Fim deve ser posterior ao início");
  if(e instanceof Evento x && x.fim.isBefore(x.inicio))bad("Fim deve ser igual ou posterior ao início");
  if(e instanceof Nota x)enrolled(x.disciplinaId,x.alunoId);
  if(e instanceof Presenca x){Aula l=lessons.findById(x.aulaId).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Aula não encontrada"));enrolled(l.disciplinaId,x.alunoId);if(x.faltas>l.horas)bad("Faltas não podem exceder horas da aula");}
  if(e instanceof NotaAvaliacao x){Avaliacao v=assessments.findById(x.avaliacaoId).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Avaliação não encontrada"));enrolled(v.disciplinaId,x.alunoId);if(x.valor>v.valor)bad("Nota excede o valor da avaliação");}
 }
}
