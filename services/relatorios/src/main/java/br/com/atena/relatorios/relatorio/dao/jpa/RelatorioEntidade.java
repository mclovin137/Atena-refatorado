package br.com.atena.relatorios.relatorio.dao.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "Relatorio")
@Table(name = "relatorio")
public class RelatorioEntidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public Long alunoId;
    public Long ownerId;
    public Long professorId;
    public boolean releasedOnly;
    public String tipo;
    public String requestId;
    public Long executionId;
    public String nome;

    @Column(columnDefinition = "text")
    public String snapshot;
}
