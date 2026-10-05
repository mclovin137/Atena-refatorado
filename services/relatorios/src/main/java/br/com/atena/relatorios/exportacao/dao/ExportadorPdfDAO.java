package br.com.atena.relatorios.exportacao.dao;

import br.com.atena.relatorios.exportacao.builder.TextoRelatorioBuilder;
import br.com.atena.relatorios.exportacao.dto.ArquivoRelatorioDTO;
import br.com.atena.relatorios.exportacao.dto.DocumentoRelatorioDTO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Repository
public class ExportadorPdfDAO implements IExportadorRelatorioDAO {
    private final TextoRelatorioBuilder textos = new TextoRelatorioBuilder();

    @Override
    public String formato() {
        return "pdf";
    }

    @Override
    public ArquivoRelatorioDTO exportar(DocumentoRelatorioDTO documento) throws IOException {
        try (PDDocument pdf = new PDDocument()) {
            List<String> linhas = textos.montar(documento);
            for (int inicio = 0; inicio < linhas.size(); inicio += 48) {
                pagina(pdf, linhas.subList(inicio, Math.min(linhas.size(), inicio + 48)));
            }
            ByteArrayOutputStream saida = new ByteArrayOutputStream();
            pdf.save(saida);
            return new ArquivoRelatorioDTO(saida.toByteArray(), "application/pdf");
        }
    }

    private void pagina(PDDocument documento, List<String> linhas) throws IOException {
        PDPage pagina = new PDPage();
        documento.addPage(pagina);
        try (PDPageContentStream conteudo = new PDPageContentStream(documento, pagina)) {
            conteudo.beginText();
            conteudo.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
            conteudo.setLeading(14);
            conteudo.newLineAtOffset(40, 750);
            for (String linha : linhas) {
                conteudo.showText(linha.replaceAll("[^\\x20-\\x7E\\xA0-\\xFF]", "?"));
                conteudo.newLine();
            }
            conteudo.endText();
        }
    }
}
