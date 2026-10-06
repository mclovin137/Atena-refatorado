package br.com.atena.escolar.requisitomateria.dao.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RequisitoMateriaChaveJpa implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "requisito_id")
    public Long requisitoId;

    @Column(name = "materia_id")
    public Long materiaId;

    public RequisitoMateriaChaveJpa() {}

    public RequisitoMateriaChaveJpa(Long requisitoId, Long materiaId) {
        this.requisitoId = requisitoId;
        this.materiaId = materiaId;
    }

    @Override
    public boolean equals(Object outro) {
        if (this == outro) return true;
        if (!(outro instanceof RequisitoMateriaChaveJpa chave)) return false;
        return Objects.equals(requisitoId, chave.requisitoId)
                && Objects.equals(materiaId, chave.materiaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requisitoId, materiaId);
    }
}
