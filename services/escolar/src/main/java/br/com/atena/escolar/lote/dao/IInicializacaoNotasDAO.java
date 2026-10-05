package br.com.atena.escolar.lote.dao;

import br.com.atena.escolar.lote.dominio.ExecucaoLote;

import java.util.Optional;

/** Fronteira da execução Batch, sem tipos Spring na aplicação. */
public interface IInicializacaoNotasDAO {
    ExecucaoLote iniciar(Long turmaId, String requisicaoId);

    Optional<ExecucaoLote> consultar(Long identificador);
}
