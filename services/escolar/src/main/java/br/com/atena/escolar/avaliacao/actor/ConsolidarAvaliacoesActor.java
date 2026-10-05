package br.com.atena.escolar.avaliacao.actor;

import br.com.atena.escolar.autorizacao.actor.AutorizacaoEnsinoActor;
import br.com.atena.escolar.avaliacao.dao.IAvaliacaoDAO;
import br.com.atena.escolar.avaliacao.dto.ConsolidarAvaliacoesRequisicaoDTO;
import br.com.atena.escolar.avaliacao.service.AgregacaoNotasFactory;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.disciplina.dao.IDisciplinaDAO;
import br.com.atena.escolar.matricula.dao.IMatriculaDAO;
import br.com.atena.escolar.nota.actor.AutorizacaoNotaActor;
import br.com.atena.escolar.nota.builder.NotaBuilder;
import br.com.atena.escolar.nota.dao.INotaDAO;
import br.com.atena.escolar.nota.dominio.Nota;
import br.com.atena.escolar.nota.dto.NotaRespostaDTO;
import br.com.atena.escolar.notaavaliacao.dao.INotaAvaliacaoDAO;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class ConsolidarAvaliacoesActor {
    private final IAvaliacaoDAO avaliacoes;
    private final INotaAvaliacaoDAO notasAvaliacoes;
    private final INotaDAO notas;
    private final IMatriculaDAO matriculas;
    private final IDisciplinaDAO disciplinas;
    private final AutorizacaoEnsinoActor autorizacao;
    private final AutorizacaoNotaActor autorizacaoNotas;
    private final AgregacaoNotasFactory agregacoes = new AgregacaoNotasFactory();

    public ConsolidarAvaliacoesActor(
            IAvaliacaoDAO avaliacoes,
            INotaAvaliacaoDAO notasAvaliacoes,
            INotaDAO notas,
            IMatriculaDAO matriculas,
            IDisciplinaDAO disciplinas,
            AutorizacaoEnsinoActor autorizacao,
            AutorizacaoNotaActor autorizacaoNotas) {
        this.avaliacoes = avaliacoes;
        this.notasAvaliacoes = notasAvaliacoes;
        this.notas = notas;
        this.matriculas = matriculas;
        this.disciplinas = disciplinas;
        this.autorizacao = autorizacao;
        this.autorizacaoNotas = autorizacaoNotas;
    }

    @Transactional
    public List<NotaRespostaDTO> executar(
            Long disciplinaId, ConsolidarAvaliacoesRequisicaoDTO requisicao) {
        autorizacao.exigirDisciplina(disciplinaId, true);
        var disciplina = disciplinas.buscar(disciplinaId).orElseThrow();
        var qualitativas =
                avaliacoes.listar().stream()
                        .filter(
                                avaliacao ->
                                        avaliacao.disciplinaId().equals(disciplinaId)
                                                && avaliacao.bimestre() == requisicao.bimestre()
                                                && "QUALITATIVA".equals(avaliacao.tipo()))
                        .toList();
        if (qualitativas.size() < 2) {
            throw ErroAplicacao.entrada("São necessárias ao menos duas avaliações qualitativas");
        }
        var estrategia = agregacoes.selecionar(requisicao.modo());
        List<NotaRespostaDTO> respostas = new ArrayList<>();
        for (var matricula : matriculas.daTurma(disciplina.turmaId())) {
            List<Double> valores = new ArrayList<>();
            for (var avaliacao : qualitativas) {
                notasAvaliacoes.daAvaliacao(avaliacao.id()).stream()
                        .filter(nota -> nota.alunoId().equals(matricula.alunoId()))
                        .findFirst()
                        .ifPresent(nota -> valores.add(nota.valor()));
            }
            if (valores.isEmpty()) {
                continue;
            }
            var anterior =
                    notas.doAlunoNaDisciplina(disciplinaId, matricula.alunoId()).stream()
                            .filter(nota -> nota.bimestre() == requisicao.bimestre())
                            .findFirst()
                            .orElseGet(
                                    () ->
                                            new Nota(
                                                    null,
                                                    disciplinaId,
                                                    matricula.alunoId(),
                                                    requisicao.bimestre(),
                                                    null,
                                                    null,
                                                    null,
                                                    false));
            autorizacaoNotas.exigir(anterior, true);
            var consolidada =
                    new Nota(
                            anterior.id(),
                            anterior.disciplinaId(),
                            anterior.alunoId(),
                            anterior.bimestre(),
                            estrategia.calcular(valores),
                            anterior.recuperacao(),
                            anterior.prazo(),
                            anterior.liberada());
            respostas.add(NotaBuilder.resposta(notas.salvar(consolidada)));
        }
        return respostas;
    }
}
