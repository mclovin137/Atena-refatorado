package br.com.atena.escolar.disciplina.dao.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity(name = "DisciplinaEntidadeJpa")
@Table(name = "disciplina")
public class DisciplinaEntidadeJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotNull public Long turmaId;
    @NotNull public Long materiaId;

    public Long professorId;

    @Min(1)
    @Max(2000)
    public int cargaHoraria;
}
