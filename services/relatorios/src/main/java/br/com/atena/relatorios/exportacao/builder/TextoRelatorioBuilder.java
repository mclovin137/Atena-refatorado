package br.com.atena.relatorios.exportacao.builder;

import br.com.atena.relatorios.exportacao.dto.DocumentoRelatorioDTO;
import br.com.atena.relatorios.snapshot.dto.LinhaSnapshotDTO;
import br.com.atena.relatorios.snapshot.dto.ResultadoSnapshotDTO;
import br.com.atena.relatorios.snapshot.dto.SnapshotDTO;
import br.com.atena.relatorios.snapshot.dto.UnidadeSnapshotDTO;

import java.util.ArrayList;
import java.util.List;

public class TextoRelatorioBuilder {
    public List<String> montar(DocumentoRelatorioDTO documento) {
        List<String> textos = new ArrayList<>();
        textos.add("ATENA - " + documento.tipo());
        textos.add("Aluno / lista: " + documento.nome());
        cabecalho(documento.snapshot(), textos);
        textos.add("Gerado pela execução " + documento.executionId());
        for (LinhaSnapshotDTO linha : documento.linhas()) {
            String texto = descrever(linha);
            for (int inicio = 0; inicio < texto.length(); inicio += 85) {
                textos.add(texto.substring(inicio, Math.min(texto.length(), inicio + 85)));
            }
            textos.add("");
        }
        return textos;
    }

    private void cabecalho(SnapshotDTO snapshot, List<String> textos) {
        if (snapshot == null) {
            return;
        }
        UnidadeSnapshotDTO unidade = snapshot.unidade();
        if (unidade != null && unidade.nome() != null) {
            textos.add("Unidade escolar: " + unidade.nome());
            incluirDocumento(textos, "CNPJ: ", unidade.cnpj());
            textos.add("Endereço: " + unidade.endereco() + " | Telefone: " + unidade.telefone());
        }
        if (snapshot.matricula() != null) {
            textos.add("Matrícula: " + snapshot.matricula());
        }
        incluirDocumento(textos, "CPF / documento: ", snapshot.documento());
    }

    private void incluirDocumento(List<String> textos, String rotulo, String documento) {
        if (documento != null && !documento.isBlank()) {
            textos.add(rotulo + documento);
        }
    }

    private String descrever(LinhaSnapshotDTO linha) {
        if (linha.disciplina() == null) {
            return "Nome: "
                    + ausente(linha.nome())
                    + " | Matrícula: "
                    + ausente(linha.matricula())
                    + " | Ano: "
                    + ausente(linha.ano());
        }
        ResultadoSnapshotDTO resultado = linha.resultado();
        return linha.disciplina()
                + " | Turma: "
                + ausente(linha.turma())
                + " | Ano: "
                + linha.ano()
                + " | Bimestres: "
                + linha.notas()
                + " | Recuperação: "
                + ausente(linha.recuperacoes())
                + " | Média anual: "
                + resultado.media()
                + " | Média parcial: "
                + ausente(resultado.parcial())
                + " | Frequência: "
                + ausente(resultado.frequencia())
                + "% | Situação: "
                + resultado.situacao().replace('_', ' ');
    }

    private Object ausente(Object valor) {
        return valor == null ? "" : valor;
    }
}
