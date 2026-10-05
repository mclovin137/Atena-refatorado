package br.com.atena.escolar.disciplina.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IDisciplinaRepositorioJpa extends JpaRepository<DisciplinaEntidadeJpa, Long> {
    List<DisciplinaEntidadeJpa> findByTurmaId(Long turmaId);
}
