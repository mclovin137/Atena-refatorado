package br.com.atena.relatorios.exportacao.actor;

import br.com.atena.relatorios.exportacao.dao.IExportadorRelatorioDAO;
import br.com.atena.relatorios.relatorio.dominio.ErroRelatorio;

import org.springframework.stereotype.Component;

import java.util.List;

/** Seleciona três formatos existentes; nenhum formato cria outro caminho de endpoint. */
@Component
public class ExportadorRelatorioFactory {
    private final List<IExportadorRelatorioDAO> exportadores;

    public ExportadorRelatorioFactory(List<IExportadorRelatorioDAO> exportadores) {
        this.exportadores = List.copyOf(exportadores);
    }

    public IExportadorRelatorioDAO selecionar(String formato) {
        return exportadores.stream()
                .filter(exportador -> exportador.formato().equals(formato))
                .findFirst()
                .orElseThrow(
                        () ->
                                new ErroRelatorio(
                                        ErroRelatorio.CategoriaEnum.INVALIDO, "Formato inválido"));
    }
}
