package br.com.atena.relatorios.exportacao.dto;

import br.com.atena.relatorios.snapshot.dto.LinhaSnapshotDTO;

import java.util.List;

public record ExportacaoJsonDTO(String nome, String tipo, List<LinhaSnapshotDTO> linhas) {}
