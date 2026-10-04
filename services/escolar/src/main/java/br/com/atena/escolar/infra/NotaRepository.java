package br.com.atena.escolar.infra;
public interface NotaRepository extends org.springframework.data.jpa.repository.JpaRepository<Nota,Long> { java.util.List<Nota> findByDisciplinaIdAndAlunoIdOrderByBimestre(Long disciplinaId, Long alunoId); boolean existsByDisciplinaIdAndAlunoIdAndBimestre(Long disciplinaId, Long alunoId, int bimestre); }
