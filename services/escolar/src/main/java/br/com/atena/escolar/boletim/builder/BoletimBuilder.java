package br.com.atena.escolar.boletim.builder;

import br.com.atena.escolar.boletim.dominio.BoletimPreparado;
import br.com.atena.escolar.boletim.dto.BoletimRespostaDTO;
import br.com.atena.escolar.boletim.dto.DisciplinaBoletimDTO;
import br.com.atena.escolar.boletim.dto.ResultadoBoletimDTO;
import br.com.atena.escolar.boletim.dto.UnidadeBoletimDTO;

import java.util.Objects;

public final class BoletimBuilder {
    private BoletimBuilder() {}

    public static BoletimRespostaDTO resposta(BoletimPreparado dados) {
        var unidade = dados.unidade();
        var escola =
                unidade == null
                        ? new UnidadeBoletimDTO(null, null, null, null)
                        : new UnidadeBoletimDTO(
                                unidade.nome(),
                                Objects.toString(unidade.endereco(), ""),
                                Objects.toString(unidade.telefone(), ""),
                                Objects.toString(unidade.cnpj(), ""));
        var linhas =
                dados.linhas().stream()
                        .map(
                                linha -> {
                                    var resultado = linha.resultado();
                                    return new DisciplinaBoletimDTO(
                                            linha.turma(),
                                            linha.ano(),
                                            linha.disciplina(),
                                            linha.disciplinaId(),
                                            linha.cargaHoraria(),
                                            linha.dependencia(),
                                            linha.notas(),
                                            linha.recuperacoes(),
                                            linha.faltas(),
                                            new ResultadoBoletimDTO(
                                                    resultado.media(),
                                                    resultado.parcial(),
                                                    resultado.frequencia(),
                                                    resultado.situacao()));
                                })
                        .toList();
        return new BoletimRespostaDTO(
                dados.alunoId(),
                dados.nome(),
                dados.matricula(),
                dados.documento(),
                escola,
                linhas);
    }
}
