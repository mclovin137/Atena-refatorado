package br.com.atena.relatorios.exportacao.dto;

import br.com.atena.relatorios.snapshot.dto.LinhaSnapshotDTO;
import br.com.atena.relatorios.snapshot.dto.SnapshotDTO;

import java.util.List;

public record DocumentoRelatorioDTO(
        String nome,
        String tipo,
        Long executionId,
        SnapshotDTO snapshot,
        List<LinhaSnapshotDTO> linhas) {}
