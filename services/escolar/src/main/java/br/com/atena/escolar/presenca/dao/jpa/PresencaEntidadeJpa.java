package br.com.atena.escolar.presenca.dao.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity(name = "PresencaEntidadeJpa")
@Table(name = "presenca")
public class PresencaEntidadeJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotNull public Long aulaId;
    @NotNull public Long alunoId;

    @Min(0)
    @Max(12)
    public int faltas;
}
