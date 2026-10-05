package br.com.atena.escolar.aula.dao.jpa;

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

import java.time.LocalDate;

@Entity(name = "AulaEntidadeJpa")
@Table(name = "aula")
public class AulaEntidadeJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotNull public Long disciplinaId;
    @NotNull public LocalDate data;

    @NotBlank
    @Size(max = 4000)
    public String conteudo;

    @Size(max = 4000)
    public String planejamento;

    @Min(1)
    @Max(12)
    public int horas;
}
