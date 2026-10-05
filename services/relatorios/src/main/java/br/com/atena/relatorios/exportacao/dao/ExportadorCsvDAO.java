package br.com.atena.relatorios.exportacao.dao;

import br.com.atena.relatorios.exportacao.dto.ArquivoRelatorioDTO;
import br.com.atena.relatorios.exportacao.dto.DocumentoRelatorioDTO;
import br.com.atena.relatorios.exportacao.helper.CampoCsvHelper;
import br.com.atena.relatorios.snapshot.dto.LinhaSnapshotDTO;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Repository
public class ExportadorCsvDAO implements IExportadorRelatorioDAO {
    private final ObjectMapper mapper;

    public ExportadorCsvDAO(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public String formato() {
        return "csv";
    }

    @Override
    public ArquivoRelatorioDTO exportar(DocumentoRelatorioDTO documento) throws IOException {
        StringBuilder conteudo = new StringBuilder("registro;conteudo\n");
        int numero = 1;
        for (LinhaSnapshotDTO linha : documento.linhas()) {
            conteudo.append(numero++)
                    .append(';')
                    .append(CampoCsvHelper.escapar(mapper.writeValueAsString(linha)))
                    .append('\n');
        }
        return new ArquivoRelatorioDTO(
                conteudo.toString().getBytes(StandardCharsets.UTF_8), "text/csv;charset=UTF-8");
    }
}
