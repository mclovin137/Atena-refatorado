package br.com.atena.escolar.notaavaliacao.actor;

import br.com.atena.escolar.autorizacao.actor.AutorizacaoEnsinoActor;
import br.com.atena.escolar.avaliacao.dao.IAvaliacaoDAO;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.matricula.actor.ValidarVinculoEnsinoActor;
import br.com.atena.escolar.nota.actor.AutorizacaoNotaActor;
import br.com.atena.escolar.notaavaliacao.dominio.NotaAvaliacao;
import br.com.atena.escolar.notaavaliacao.service.ValidarNotaAvaliacaoService;

import org.springframework.stereotype.Component;

@Component
public class AutorizacaoNotaAvaliacaoActor {

    private final IAvaliacaoDAO avaliacoes;
    private final AutorizacaoEnsinoActor autorizacao;
    private final AutorizacaoNotaActor notas;
    private final ValidarVinculoEnsinoActor vinculos;

    public AutorizacaoNotaAvaliacaoActor(
            IAvaliacaoDAO avaliacoes,
            AutorizacaoEnsinoActor autorizacao,
            AutorizacaoNotaActor notas,
            ValidarVinculoEnsinoActor vinculos) {
        this.avaliacoes = avaliacoes;
        this.autorizacao = autorizacao;
        this.notas = notas;
        this.vinculos = vinculos;
    }

    public void exigir(NotaAvaliacao nota, boolean escrita) {
        var usuario = autorizacao.usuario();
        if (usuario.secretaria()) {
            return;
        }
        var avaliacao =
                avaliacoes
                        .buscar(nota.avaliacaoId())
                        .orElseThrow(() -> ErroAplicacao.ausente("Avaliação não encontrada"));
        autorizacao.exigirAluno(nota.alunoId(), avaliacao.disciplinaId(), escrita);
        if (escrita) {
            notas.exigirPeriodoAberto(
                    avaliacao.disciplinaId(), nota.alunoId(), avaliacao.bimestre());
        }
        if (usuario.aluno()
                && !notas.periodoLiberado(
                        avaliacao.disciplinaId(), nota.alunoId(), avaliacao.bimestre())) {
            ErroAplicacao.negar();
        }
    }

    public void validarVinculos(NotaAvaliacao nota) {
        var avaliacao =
                avaliacoes
                        .buscar(nota.avaliacaoId())
                        .orElseThrow(() -> ErroAplicacao.entrada("Avaliação não encontrada"));
        vinculos.exigirMatricula(avaliacao.disciplinaId(), nota.alunoId());
        ValidarNotaAvaliacaoService.validar(nota.valor(), avaliacao.valor());
    }
}
