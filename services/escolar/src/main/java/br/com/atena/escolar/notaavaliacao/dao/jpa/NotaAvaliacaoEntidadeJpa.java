package br.com.atena.escolar.notaavaliacao.dao.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Entity(name = "NotaAvaliacaoEntidadeJpa")
@Table(name = "nota_avaliacao")
public class NotaAvaliacaoEntidadeJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotNull public Long avaliacaoId;
    @NotNull public Long alunoId;

    @NotNull
    @DecimalMin("0")
    @DecimalMax("100")
    public Double valor;
}
