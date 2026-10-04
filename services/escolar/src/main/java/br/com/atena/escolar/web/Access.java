package br.com.atena.escolar.web;
import br.com.atena.escolar.infra.*;
import java.util.*;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
@Component
public class Access {
 final PessoaRepository people; final DisciplinaRepository disciplines; final MatriculaRepository enrollments;final AulaRepository lessons;final AvaliacaoRepository assessments;final NotaRepository grades;
 public Access(PessoaRepository p,DisciplinaRepository d,MatriculaRepository m,AulaRepository a,AvaliacaoRepository v,NotaRepository g){grades=g;people=p;disciplines=d;enrollments=m;lessons=a;assessments=v;}
 public Pessoa person(Authentication auth){if(auth==null)throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Autenticação necessária");return people.findByEmail(auth.getName()).filter(p->p.ativo).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Conta inativa"));}
 public boolean secretary(Authentication a){return "SECRETARIA".equals(person(a).perfil);}
 public void admin(Authentication a){if(!secretary(a))deny();}
 public void deny(){throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Acesso negado");}
 public void discipline(Long id,Authentication a,boolean write){Disciplina d=disciplines.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Disciplina não encontrada"));Pessoa p=person(a);if(secretary(a))return;if(p.perfil.equals("PROFESSOR") && p.id.equals(d.professorId))return;if(!write && p.perfil.equals("ALUNO") && enrollments.existsByTurmaIdAndAlunoId(d.turmaId,p.id))return;deny();}
 public void student(Long alunoId,Long disciplinaId,Authentication a,boolean write){discipline(disciplinaId,a,write);Pessoa p=person(a);if(p.perfil.equals("ALUNO")&&!p.id.equals(alunoId))deny();Disciplina d=disciplines.findById(disciplinaId).orElseThrow();if(!enrollments.existsByTurmaIdAndAlunoId(d.turmaId,alunoId))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Aluno não matriculado na turma");}
 public void periodOpen(Long discipline,Long student,int bimestre){Nota n=grades.findByDisciplinaIdAndAlunoIdOrderByBimestre(discipline,student).stream().filter(g->g.bimestre==bimestre).findFirst().orElseThrow(()->new ResponseStatusException(HttpStatus.CONFLICT,"Secretaria deve preparar e liberar notas antes do lançamento"));if(!n.liberada||n.prazo==null||java.time.LocalDate.now().isAfter(n.prazo))throw new ResponseStatusException(HttpStatus.CONFLICT,"Notas fechadas ou prazo encerrado; solicite reabertura à secretaria");}
 public boolean readable(Object e,Authentication a){try{check(e,a,false);return true;}catch(ResponseStatusException ex){return false;}}
 public void check(Object e,Authentication a,boolean write){
  Pessoa p=person(a); if(secretary(a))return;
  if(e instanceof Pessoa x){if(write||!x.id.equals(p.id))deny();}
  else if(e instanceof Disciplina x)discipline(x.id,a,write);
  else if(e instanceof Matricula x){if(write)deny(); if(p.perfil.equals("ALUNO")&&!p.id.equals(x.alunoId))deny();if(p.perfil.equals("PROFESSOR")&&!disciplines.findByTurmaId(x.turmaId).stream().anyMatch(d->p.id.equals(d.professorId)))deny();}
  else if(e instanceof Horario x)discipline(x.disciplinaId,a,write);
  else if(e instanceof Aula x)discipline(x.disciplinaId,a,write);
  else if(e instanceof Avaliacao x)discipline(x.disciplinaId,a,write);
  else if(e instanceof Nota x){student(x.alunoId,x.disciplinaId,a,write);if(p.perfil.equals("ALUNO")&&!x.liberada)deny();if(write){if(x.id==null)deny();Nota old=grades.findById(x.id).orElseThrow();if(!old.disciplinaId.equals(x.disciplinaId)||!old.alunoId.equals(x.alunoId)||old.bimestre!=x.bimestre||!java.util.Objects.equals(old.prazo,x.prazo)||old.liberada!=x.liberada)deny();if(!old.liberada||old.prazo==null||java.time.LocalDate.now().isAfter(old.prazo))throw new ResponseStatusException(HttpStatus.CONFLICT,"Notas fechadas ou prazo encerrado; solicite reabertura à secretaria");}}
  else if(e instanceof NotaAvaliacao x){Avaliacao av=assessments.findById(x.avaliacaoId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Avaliação não encontrada"));student(x.alunoId,av.disciplinaId,a,write);if(write)periodOpen(av.disciplinaId,x.alunoId,av.bimestre);if(p.perfil.equals("ALUNO")&&grades.findByDisciplinaIdAndAlunoIdOrderByBimestre(av.disciplinaId,x.alunoId).stream().noneMatch(n->n.bimestre==av.bimestre&&n.liberada))deny();}
  else if(e instanceof Presenca x){Aula l=lessons.findById(x.aulaId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Aula não encontrada"));student(x.alunoId,l.disciplinaId,a,write);}
  else if(e instanceof Mensagem x){if(write && !p.id.equals(x.remetenteId))deny();if(!write&&!p.id.equals(x.remetenteId)&&!p.id.equals(x.destinatarioId))deny();}
  else if(e instanceof Material x)discipline(x.disciplinaId,a,write);
  else if(e instanceof Turma x){if(write)deny();if(p.perfil.equals("ALUNO")&&!enrollments.existsByTurmaIdAndAlunoId(x.id,p.id))deny();if(p.perfil.equals("PROFESSOR")&&!disciplines.findByTurmaId(x.id).stream().anyMatch(d->p.id.equals(d.professorId)))deny();}
  else if(write)deny();
 }
}
