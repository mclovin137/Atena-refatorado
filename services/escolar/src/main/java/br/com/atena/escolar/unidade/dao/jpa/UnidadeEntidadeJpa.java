package br.com.atena.escolar.unidade.dao.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity(name = "UnidadeEntidadeJpa")
@Table(name = "unidade")
public class UnidadeEntidadeJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotBlank
    @Size(max = 150)
    public String nome;

    @Size(max = 500)
    public String endereco;

    @Size(max = 30)
    public String telefone;

    @Size(max = 18)
    public String cnpj;
}
