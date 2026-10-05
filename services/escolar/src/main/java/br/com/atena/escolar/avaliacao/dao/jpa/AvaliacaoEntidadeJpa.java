package br.com.atena.escolar.avaliacao.dao.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity(name = "AvaliacaoEntidadeJpa")
@Table(name = "avaliacao")
public class AvaliacaoEntidadeJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotNull public Long disciplinaId;

    @NotBlank
    @Size(max = 150)
    public String nome;

    @Min(1)
    @Max(4)
    public int bimestre;

    @NotNull
    @DecimalMin("0.01")
    @DecimalMax("100")
    public Double valor;

    @NotNull public LocalDate data;

    @NotBlank
    @Pattern(regexp = "QUANTITATIVA|QUALITATIVA")
    public String tipo;
}
