package br.com.atena.escolar.material.dao.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity(name = "MaterialEntidadeJpa")
@Table(name = "material")
public class MaterialEntidadeJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotNull public Long disciplinaId;

    @NotBlank
    @Size(max = 150)
    public String nome;

    public String tipo;
    @JsonIgnore public String arquivo;

    public Long autorId;

    @Column(name = "conteudo", columnDefinition = "bytea")
    public byte[] conteudo;

    public MaterialEntidadeJpa() {}

    public MaterialEntidadeJpa(
            Long id, Long disciplinaId, String nome, String tipo, String arquivo, Long autorId) {
        this.id = id;
        this.disciplinaId = disciplinaId;
        this.nome = nome;
        this.tipo = tipo;
        this.arquivo = arquivo;
        this.autorId = autorId;
    }
}
