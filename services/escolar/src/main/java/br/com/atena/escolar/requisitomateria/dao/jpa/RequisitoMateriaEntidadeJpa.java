package br.com.atena.escolar.requisitomateria.dao.jpa;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "requisito_materia")
public class RequisitoMateriaEntidadeJpa {
    @EmbeddedId public RequisitoMateriaChaveJpa chave;
}
