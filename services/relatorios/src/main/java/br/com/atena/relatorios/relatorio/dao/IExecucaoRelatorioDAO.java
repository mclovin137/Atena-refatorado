package br.com.atena.relatorios.relatorio.dao;

import br.com.atena.relatorios.relatorio.dominio.ExecucaoRelatorio;

public interface IExecucaoRelatorioDAO {
    ExecucaoRelatorio buscar(Long executionId);

    ExecucaoRelatorio iniciar(Long reportId);
}
