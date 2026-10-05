package br.com.atena.escolar.nota.dao.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity(name = "NotaEntidadeJpa")
@Table(name = "nota")
public class NotaEntidadeJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotNull public Long disciplinaId;
    @NotNull public Long alunoId;

    @Min(1)
    @Max(4)
    public int bimestre;

    @DecimalMin("0")
    @DecimalMax("100")
    public Double valor;

    @DecimalMin("0")
    @DecimalMax("60")
    public Double recuperacao;

    public LocalDate prazo;

    public boolean liberada;
}
