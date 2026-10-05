package br.com.atena.relatorios.lote.dao;

import br.com.atena.relatorios.linha.dao.ILinhaDAO;
import br.com.atena.relatorios.lote.dto.LinhaLoteDTO;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class GravadorLinhas implements ItemWriter<LinhaLoteDTO> {
    private final ILinhaDAO linhas;

    public GravadorLinhas(ILinhaDAO linhas) {
        this.linhas = linhas;
    }

    @Override
    public void write(Chunk<? extends LinhaLoteDTO> registros) throws Exception {
        for (LinhaLoteDTO registro : registros) {
            linhas.gravar(registro.reportId(), registro.numero(), registro.conteudo());
        }
    }
}
