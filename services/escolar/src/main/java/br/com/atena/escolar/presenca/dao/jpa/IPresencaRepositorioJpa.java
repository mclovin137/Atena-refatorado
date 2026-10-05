package br.com.atena.escolar.presenca.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPresencaRepositorioJpa extends JpaRepository<PresencaEntidadeJpa, Long> {
    List<PresencaEntidadeJpa> findByAulaId(Long aulaId);
}
