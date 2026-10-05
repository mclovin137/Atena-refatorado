package br.com.atena.relatorios.relatorio.dao;

import br.com.atena.relatorios.relatorio.dominio.Relatorio;
import br.com.atena.relatorios.relatorio.dominio.SolicitacaoRelatorio;

import java.util.Optional;

public interface IRelatorioDAO {
    Optional<Relatorio> buscar(Long id);

    Optional<Relatorio> buscarSolicitacao(Long ownerId, String requestId);

    Relatorio criar(SolicitacaoRelatorio solicitacao);

    void registrarExecucao(Long id, Long executionId);

    void registrarSnapshot(Long id, String nome, String snapshot);
}
