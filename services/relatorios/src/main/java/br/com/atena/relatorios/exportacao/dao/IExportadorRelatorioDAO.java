package br.com.atena.relatorios.exportacao.dao;

import br.com.atena.relatorios.exportacao.dto.ArquivoRelatorioDTO;
import br.com.atena.relatorios.exportacao.dto.DocumentoRelatorioDTO;

import java.io.IOException;

public interface IExportadorRelatorioDAO {
    String formato();

    ArquivoRelatorioDTO exportar(DocumentoRelatorioDTO documento) throws IOException;
}
