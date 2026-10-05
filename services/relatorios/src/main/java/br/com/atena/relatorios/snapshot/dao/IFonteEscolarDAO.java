package br.com.atena.relatorios.snapshot.dao;

import br.com.atena.relatorios.relatorio.dominio.SolicitacaoRelatorio;
import br.com.atena.relatorios.snapshot.dto.SnapshotDTO;

public interface IFonteEscolarDAO {
    SnapshotDTO carregar(SolicitacaoRelatorio solicitacao);
}
