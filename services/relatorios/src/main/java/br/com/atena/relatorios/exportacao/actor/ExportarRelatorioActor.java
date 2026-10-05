package br.com.atena.relatorios.exportacao.actor;

import br.com.atena.relatorios.exportacao.dto.ArquivoRelatorioDTO;
import br.com.atena.relatorios.exportacao.dto.DocumentoRelatorioDTO;
import br.com.atena.relatorios.linha.dao.ILinhaDAO;
import br.com.atena.relatorios.relatorio.actor.ConsultarRelatorioActor;
import br.com.atena.relatorios.relatorio.dominio.Relatorio;
import br.com.atena.relatorios.snapshot.dao.SnapshotJsonDAO;
import br.com.atena.relatorios.snapshot.dto.SnapshotDTO;

import org.springframework.stereotype.Component;

@Component
public class ExportarRelatorioActor {
    private final ConsultarRelatorioActor consulta;
    private final ILinhaDAO linhas;
    private final SnapshotJsonDAO json;
    private final ExportadorRelatorioFactory formatos;

    public ExportarRelatorioActor(
            ConsultarRelatorioActor consulta,
            ILinhaDAO linhas,
            SnapshotJsonDAO json,
            ExportadorRelatorioFactory formatos) {
        this.consulta = consulta;
        this.linhas = linhas;
        this.json = json;
        this.formatos = formatos;
    }

    public ArquivoRelatorioDTO exportar(Long id, String formato) throws java.io.IOException {
        Relatorio relatorio = consulta.concluido(id);
        SnapshotDTO snapshot =
                relatorio.snapshot() == null ? null : json.lerSnapshot(relatorio.snapshot());
        DocumentoRelatorioDTO documento =
                new DocumentoRelatorioDTO(
                        relatorio.nome(),
                        relatorio.solicitacao().tipo().name(),
                        relatorio.executionId(),
                        snapshot,
                        linhas.listar(id));
        return formatos.selecionar(formato).exportar(documento);
    }
}
