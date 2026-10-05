package br.com.atena.escolar.aula.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IAulaRepositorioJpa extends JpaRepository<AulaEntidadeJpa, Long> {
    List<AulaEntidadeJpa> findByDisciplinaId(Long disciplinaId);
}
