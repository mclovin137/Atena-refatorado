package br.com.atena.escolar.gradecurricular.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IGradeCurricularRepositorioJpa
        extends JpaRepository<GradeCurricularEntidadeJpa, Long> {}
