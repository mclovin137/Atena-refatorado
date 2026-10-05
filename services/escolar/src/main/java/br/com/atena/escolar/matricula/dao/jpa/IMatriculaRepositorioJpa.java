package br.com.atena.escolar.matricula.dao.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IMatriculaRepositorioJpa extends JpaRepository<MatriculaEntidadeJpa, Long> {
    List<MatriculaEntidadeJpa> findByTurmaId(Long turmaId);

    List<MatriculaEntidadeJpa> findByAlunoId(Long alunoId);

    boolean existsByTurmaIdAndAlunoId(Long turmaId, Long alunoId);

    Page<MatriculaEntidadeJpa> findByTurmaId(Long turmaId, Pageable pageable);
}
