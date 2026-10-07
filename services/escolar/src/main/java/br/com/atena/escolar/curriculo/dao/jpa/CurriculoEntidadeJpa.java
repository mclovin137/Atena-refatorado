package br.com.atena.escolar.curriculo.dao.jpa;

import jakarta.persistence.Column;
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

@Entity(name = "CurriculoEntidadeJpa")
@Table(name = "curriculo")
public class CurriculoEntidadeJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotBlank
    @Size(max = 150)
    public String nome;

    @NotNull public Long materiaId;

    @Min(1)
    @Max(2000)
    public int cargaHoraria;

    @Column(name = "grade_id")
    public Long gradeId;
}
