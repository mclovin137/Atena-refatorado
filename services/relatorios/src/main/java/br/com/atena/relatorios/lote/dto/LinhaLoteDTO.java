package br.com.atena.relatorios.lote.dto;

import br.com.atena.relatorios.snapshot.dto.LinhaSnapshotDTO;

public record LinhaLoteDTO(Long reportId, int numero, LinhaSnapshotDTO conteudo) {}
