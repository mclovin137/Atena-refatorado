package br.com.atena.escolar.matricula.dao.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity(name = "MatriculaEntidadeJpa")
@Table(name = "matricula")
public class MatriculaEntidadeJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotNull public Long turmaId;
    @NotNull public Long alunoId;

    public boolean dependencia;
}
