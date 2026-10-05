package br.com.atena.escolar.nota.service;

import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.nota.dominio.Nota;

import java.time.LocalDate;
import java.util.Objects;

/** Regras de prazo e campos imutáveis recebem estado pronto, sem banco ou HTTP. */
public final class PeriodoNotaService {

    public void exigirAberto(Nota nota, LocalDate hoje) {
        if (!nota.liberada() || nota.prazo() == null || hoje.isAfter(nota.prazo())) {
            throw ErroAplicacao.conflito(
                    "Notas fechadas ou prazo encerrado; solicite reabertura à secretaria");
        }
    }

    public void protegerMetadados(Nota anterior, Nota alterada) {
        if (!anterior.disciplinaId().equals(alterada.disciplinaId())
                || !anterior.alunoId().equals(alterada.alunoId())
                || anterior.bimestre() != alterada.bimestre()
                || !Objects.equals(anterior.prazo(), alterada.prazo())
                || anterior.liberada() != alterada.liberada()) {
            ErroAplicacao.negar();
        }
    }
}
