package br.com.atena.escolar.importacao.dao.jpa;

import br.com.atena.escolar.importacao.dominio.EstadoImportacaoEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "importacao_fonte")
public class ImportacaoFonteEntidadeJpa {
    @Id public UUID id;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(nullable = false, length = 64)
    public String digest;

    public int versao;
    public byte[] nonce;
    public byte[] conteudo;

    @Enumerated(EnumType.STRING)
    public EstadoImportacaoEnum status;

    public String operacao;

    @Column(name = "execucao_id")
    public Long execucaoId;

    @Column(name = "criado_em")
    public Instant criadoEm;

    @Column(name = "atualizado_em")
    public Instant atualizadoEm;
}
