package br.com.atena.relatorios.linha.dao;

import br.com.atena.relatorios.snapshot.dto.LinhaSnapshotDTO;

import java.util.List;

/** Fronteira de persistência do contrato de integração, não do domínio escolar. */
public interface ILinhaDAO {
    List<LinhaSnapshotDTO> listar(Long relatorioId) throws java.io.IOException;

    void gravar(Long relatorioId, int numero, LinhaSnapshotDTO conteudo) throws java.io.IOException;
}
