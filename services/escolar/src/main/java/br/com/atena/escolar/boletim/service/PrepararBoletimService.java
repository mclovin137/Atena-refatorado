package br.com.atena.escolar.boletim.service;

import br.com.atena.escolar.boletim.dominio.BoletimPreparado;
import br.com.atena.escolar.boletim.dominio.DadosBoletim;
import br.com.atena.escolar.boletim.dominio.DadosDisciplinaBoletim;
import br.com.atena.escolar.boletim.dominio.DisciplinaPreparada;
import br.com.atena.escolar.boletim.dominio.ResultadoEscolar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class PrepararBoletimService {
    private PrepararBoletimService() {}

    public static BoletimPreparado preparar(
            DadosBoletim dados, Long professorId, boolean somenteLiberadas) {
        var linhas =
                dados.disciplinas().stream()
                        .filter(
                                disciplina ->
                                        professorId == null
                                                || professorId.equals(disciplina.professorId()))
                        .map(disciplina -> disciplina(disciplina, somenteLiberadas))
                        .toList();
        var aluno = dados.aluno();
        String documento = professorId == null ? Objects.toString(aluno.documento(), "") : "";
        return new BoletimPreparado(
                aluno.id(), aluno.nome(), aluno.matricula(), documento, dados.unidade(), linhas);
    }

    private static DisciplinaPreparada disciplina(
            DadosDisciplinaBoletim dados, boolean somenteLiberadas) {
        List<Double> notas = new ArrayList<>(Arrays.asList(null, null, null, null));
        List<Double> recuperacoes = new ArrayList<>(Arrays.asList(null, null, null, null));
        for (var nota : dados.notas()) {
            if (!somenteLiberadas || nota.liberada()) {
                notas.set(nota.bimestre() - 1, nota.valor());
                recuperacoes.set(nota.bimestre() - 1, nota.recuperacao());
            }
        }
        var resultado =
                ResultadoEscolar.calcular(
                        notas, recuperacoes, dados.faltas(), dados.cargaHoraria());
        return new DisciplinaPreparada(
                dados.turma(),
                dados.ano(),
                dados.disciplina(),
                dados.disciplinaId(),
                dados.cargaHoraria(),
                dados.dependencia(),
                notas,
                recuperacoes,
                dados.faltas(),
                resultado);
    }
}
