package br.com.atena.escolar.matriculamateria.dao.jpa;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "matricula_disciplina")
public class MatriculaMateriaEntidadeJpa {
    @EmbeddedId public MatriculaMateriaChaveJpa chave;
}
