package br.com.atena.escolar.matriculamateria.dao.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MatriculaMateriaChaveJpa implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "matricula_id")
    public Long matriculaId;

    @Column(name = "disciplina_id")
    public Long disciplinaId;

    public MatriculaMateriaChaveJpa() {}

    public MatriculaMateriaChaveJpa(Long matriculaId, Long disciplinaId) {
        this.matriculaId = matriculaId;
        this.disciplinaId = disciplinaId;
    }

    @Override
    public boolean equals(Object outro) {
        if (this == outro) return true;
        if (!(outro instanceof MatriculaMateriaChaveJpa chave)) return false;
        return Objects.equals(matriculaId, chave.matriculaId)
                && Objects.equals(disciplinaId, chave.disciplinaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matriculaId, disciplinaId);
    }
}
