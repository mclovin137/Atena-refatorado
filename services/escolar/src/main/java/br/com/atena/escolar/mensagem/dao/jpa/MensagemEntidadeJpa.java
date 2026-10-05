package br.com.atena.escolar.mensagem.dao.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity(name = "MensagemEntidadeJpa")
@Table(name = "mensagem")
public class MensagemEntidadeJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public Long remetenteId;
    @NotNull public Long destinatarioId;

    @NotBlank
    @Size(max = 4000)
    public String texto;

    public LocalDateTime enviadaEm;
}
