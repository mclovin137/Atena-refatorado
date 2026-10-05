package br.com.atena.escolar.evento.dao.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity(name = "EventoEntidadeJpa")
@Table(name = "evento")
public class EventoEntidadeJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotBlank
    @Size(max = 150)
    public String titulo;

    @NotNull public LocalDate inicio;
    @NotNull public LocalDate fim;

    @Size(max = 4000)
    public String descricao;
}
