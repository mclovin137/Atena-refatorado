package br.com.atena.relatorios.exportacao.dao;

import br.com.atena.relatorios.exportacao.dto.ArquivoRelatorioDTO;
import br.com.atena.relatorios.exportacao.dto.DocumentoRelatorioDTO;
import br.com.atena.relatorios.exportacao.dto.ExportacaoJsonDTO;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public class ExportadorJsonDAO implements IExportadorRelatorioDAO {
    private final ObjectMapper mapper;

    public ExportadorJsonDAO(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public String formato() {
        return "json";
    }

    @Override
    public ArquivoRelatorioDTO exportar(DocumentoRelatorioDTO documento) throws IOException {
        ExportacaoJsonDTO resposta =
                new ExportacaoJsonDTO(documento.nome(), documento.tipo(), documento.linhas());
        return new ArquivoRelatorioDTO(mapper.writeValueAsBytes(resposta), "application/json");
    }
}
