package br.com.atena.escolar.horario.dao.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

@Entity(name = "HorarioEntidadeJpa")
@Table(name = "horario")
public class HorarioEntidadeJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotNull public Long disciplinaId;

    @Min(1)
    @Max(7)
    public int diaSemana;

    @NotNull public LocalTime inicio;
    @NotNull public LocalTime fim;
}
