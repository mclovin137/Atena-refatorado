package br.com.atena.relatorios.linha.dao.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "Linha")
@Table(name = "linha")
public class LinhaEntidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public Long relatorioId;
    public int numero;

    @Column(columnDefinition = "text")
    public String conteudo;
}
