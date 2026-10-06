package br.com.atena.escolar.requisitoformacao.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IRequisitoFormacaoRepositorioJpa
        extends JpaRepository<RequisitoFormacaoEntidadeJpa, Long> {}
