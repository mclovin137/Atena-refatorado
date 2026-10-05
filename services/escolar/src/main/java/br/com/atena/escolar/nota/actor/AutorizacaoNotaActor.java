package br.com.atena.escolar.nota.actor;

import br.com.atena.escolar.autorizacao.actor.AutorizacaoEnsinoActor;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.matricula.actor.ValidarVinculoEnsinoActor;
import br.com.atena.escolar.nota.dao.INotaDAO;
import br.com.atena.escolar.nota.dominio.Nota;
import br.com.atena.escolar.nota.service.PeriodoNotaService;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AutorizacaoNotaActor {

    private final INotaDAO notas;
    private final AutorizacaoEnsinoActor autorizacao;
    private final ValidarVinculoEnsinoActor vinculos;
    private final PeriodoNotaService periodo = new PeriodoNotaService();

    public AutorizacaoNotaActor(
            INotaDAO notas,
            AutorizacaoEnsinoActor autorizacao,
            ValidarVinculoEnsinoActor vinculos) {
        this.notas = notas;
        this.autorizacao = autorizacao;
        this.vinculos = vinculos;
    }

    public void exigir(Nota nota, boolean escrita) {
        var usuario = autorizacao.usuario();
        if (usuario.secretaria()) {
            return;
        }
        autorizacao.exigirAluno(nota.alunoId(), nota.disciplinaId(), escrita);
        if (usuario.aluno() && !nota.liberada()) {
            ErroAplicacao.negar();
        }
        if (!escrita) {
            return;
        }
        if (nota.id() == null) {
            ErroAplicacao.negar();
        }
        var anterior =
                notas.buscar(nota.id())
                        .orElseThrow(() -> ErroAplicacao.ausente("Nota não encontrada"));
        periodo.protegerMetadados(anterior, nota);
        periodo.exigirAberto(anterior, LocalDate.now());
    }

    public void exigirPeriodoAberto(Long disciplinaId, Long alunoId, int bimestre) {
        var nota =
                notas.doAlunoNaDisciplina(disciplinaId, alunoId).stream()
                        .filter(registro -> registro.bimestre() == bimestre)
                        .findFirst()
                        .orElseThrow(
                                () ->
                                        ErroAplicacao.conflito(
                                                "Secretaria deve preparar e liberar notas antes do"
                                                        + " lançamento"));
        periodo.exigirAberto(nota, LocalDate.now());
    }

    public boolean periodoLiberado(Long disciplinaId, Long alunoId, int bimestre) {
        return notas.doAlunoNaDisciplina(disciplinaId, alunoId).stream()
                .anyMatch(nota -> nota.bimestre() == bimestre && nota.liberada());
    }

    public void validarVinculos(Nota nota) {
        vinculos.exigirMatricula(nota.disciplinaId(), nota.alunoId());
    }
}
