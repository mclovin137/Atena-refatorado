package br.com.atena.escolar.turma.dao.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity(name = "TurmaEntidadeJpa")
@Table(name = "turma")
public class TurmaEntidadeJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotNull public Long cursoId;
    @NotNull public Long serieId;

    @Min(1900)
    @Max(2200)
    public int ano;

    @NotBlank
    @Size(max = 150)
    public String nome;
}
