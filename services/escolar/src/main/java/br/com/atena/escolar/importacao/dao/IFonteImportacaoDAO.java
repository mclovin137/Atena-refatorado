package br.com.atena.escolar.importacao.dao;

import br.com.atena.escolar.importacao.dominio.EstadoImportacaoEnum;
import br.com.atena.escolar.importacao.dominio.FonteCriptografada;
import br.com.atena.escolar.importacao.dto.ImportacaoRespostaDTO;

import java.util.Optional;
import java.util.UUID;

/** Porta da integração privada; DTO de metadados não pertence ao domínio acadêmico. */
public interface IFonteImportacaoDAO {
    Optional<UUID> buscarDigest(String digest);

    UUID guardar(FonteCriptografada fonte);

    FonteCriptografada fonte(UUID id);

    String operacao(UUID id);

    ImportacaoRespostaDTO consultar(UUID id);

    void iniciar(UUID id, String operacao, EstadoImportacaoEnum esperado);

    void execucao(UUID id, Long execucaoId);

    void finalizar(UUID id, EstadoImportacaoEnum estado);
}
