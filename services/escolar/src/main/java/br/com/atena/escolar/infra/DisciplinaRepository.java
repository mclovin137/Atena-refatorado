package br.com.atena.escolar.infra;
public interface DisciplinaRepository extends org.springframework.data.jpa.repository.JpaRepository<Disciplina,Long> { java.util.List<Disciplina> findByTurmaId(Long turmaId); }
