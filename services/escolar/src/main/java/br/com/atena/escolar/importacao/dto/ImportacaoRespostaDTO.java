package br.com.atena.escolar.importacao.dto;

import br.com.atena.escolar.importacao.dominio.EstadoImportacaoEnum;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/** Somente metadados; não transporta staging, fonte, nomes ou credenciais. */
public record ImportacaoRespostaDTO(
        UUID id,
        String digest,
        EstadoImportacaoEnum status,
        Long execucaoId,
        Instant criadoEm,
        List<ContagemImportacaoDTO> contagens,
        List<TabelaImportacaoDTO> tabelas,
        List<MotivoImportacaoDTO> motivos) {
    @JsonProperty("jobId")
    public Long jobId() {
        return execucaoId;
    }
}
