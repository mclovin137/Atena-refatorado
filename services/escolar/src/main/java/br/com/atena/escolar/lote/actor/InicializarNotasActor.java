package br.com.atena.escolar.lote.actor;

import br.com.atena.escolar.autorizacao.actor.AutorizacaoEnsinoActor;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.lote.builder.ExecucaoLoteBuilder;
import br.com.atena.escolar.lote.dao.IInicializacaoNotasDAO;
import br.com.atena.escolar.lote.dto.ExecucaoLoteRespostaDTO;
import br.com.atena.escolar.lote.dto.InicializarNotasRequisicaoDTO;
import br.com.atena.escolar.turma.dao.ITurmaDAO;

import org.springframework.stereotype.Component;

@Component
public class InicializarNotasActor {
    private final IInicializacaoNotasDAO execucoes;
    private final ITurmaDAO turmas;
    private final AutorizacaoEnsinoActor autorizacao;

    public InicializarNotasActor(
            IInicializacaoNotasDAO execucoes,
            ITurmaDAO turmas,
            AutorizacaoEnsinoActor autorizacao) {
        this.execucoes = execucoes;
        this.turmas = turmas;
        this.autorizacao = autorizacao;
    }

    public ExecucaoLoteRespostaDTO iniciar(InicializarNotasRequisicaoDTO requisicao) {
        autorizacao.exigirSecretaria();
        turmas.buscar(requisicao.turmaId())
                .orElseThrow(() -> ErroAplicacao.ausente("Turma não encontrada"));
        return ExecucaoLoteBuilder.resposta(
                execucoes.iniciar(requisicao.turmaId(), requisicao.requestId()));
    }

    public ExecucaoLoteRespostaDTO consultar(Long identificador) {
        autorizacao.exigirSecretaria();
        var execucao =
                execucoes
                        .consultar(identificador)
                        .orElseThrow(() -> ErroAplicacao.ausente("Execução não encontrada"));
        return ExecucaoLoteBuilder.resposta(execucao);
    }
}
