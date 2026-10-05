package br.com.atena.escolar.relatorio.dao;

import br.com.atena.escolar.relatorio.dominio.DocumentoRelatorio;
import br.com.atena.escolar.relatorio.dominio.Relatorio;
import br.com.atena.escolar.relatorio.dominio.SolicitacaoRelatorio;

public interface IRelatorioDAO {
    Relatorio iniciar(SolicitacaoRelatorio solicitacao);

    Relatorio consultar(Long id);

    DocumentoRelatorio baixar(Long id, String formato);
}
