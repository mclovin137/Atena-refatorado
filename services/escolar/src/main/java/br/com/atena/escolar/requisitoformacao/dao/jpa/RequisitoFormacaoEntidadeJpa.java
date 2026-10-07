package br.com.atena.escolar.requisitoformacao.dao.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "requisito_formacao")
public class RequisitoFormacaoEntidadeJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String nome;

    @Column(name = "grade_id")
    public Long gradeId;

    public String descricao;
}
