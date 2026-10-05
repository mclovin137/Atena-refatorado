package br.com.atena.escolar.turma.actor;

import br.com.atena.escolar.autorizacao.actor.AutorizacaoEnsinoActor;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.turma.builder.TurmaBuilder;
import br.com.atena.escolar.turma.dao.ITurmaDAO;
import br.com.atena.escolar.turma.dominio.Turma;
import br.com.atena.escolar.turma.dto.TurmaRequisicaoDTO;
import br.com.atena.escolar.turma.dto.TurmaRespostaDTO;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ManterTurmaActor {

    private final ITurmaDAO registros;
    private final AutorizacaoEnsinoActor autorizacao;

    public ManterTurmaActor(ITurmaDAO registros, AutorizacaoEnsinoActor autorizacao) {
        this.registros = registros;
        this.autorizacao = autorizacao;
    }

    public List<TurmaRespostaDTO> listar() {
        autorizacao.usuario();
        return registros.listar().stream()
                .filter(this::podeLer)
                .map(TurmaBuilder::resposta)
                .toList();
    }

    public TurmaRespostaDTO consultar(Long identificador) {
        var registro = buscar(identificador);
        exigirPermissao(registro, false);
        return TurmaBuilder.resposta(registro);
    }

    @Transactional
    public TurmaRespostaDTO criar(TurmaRequisicaoDTO requisicao) {
        if (requisicao.id != null) {
            throw ErroAplicacao.entrada("Não informe id ao criar");
        }
        var registro = TurmaBuilder.dominio(requisicao, null);
        exigirPermissao(registro, true);
        return TurmaBuilder.resposta(registros.salvar(registro));
    }

    @Transactional
    public TurmaRespostaDTO atualizar(Long identificador, TurmaRequisicaoDTO requisicao) {
        exigirPermissao(buscar(identificador), true);
        var registro = TurmaBuilder.dominio(requisicao, identificador);
        exigirPermissao(registro, true);
        return TurmaBuilder.resposta(registros.salvar(registro));
    }

    @Transactional
    public void excluir(Long identificador) {
        exigirPermissao(buscar(identificador), true);
        registros.excluir(identificador);
    }

    private Turma buscar(Long identificador) {
        return registros
                .buscar(identificador)
                .orElseThrow(() -> ErroAplicacao.ausente("Registro não encontrado"));
    }

    private boolean podeLer(Turma registro) {
        try {
            exigirPermissao(registro, false);
            return true;
        } catch (ErroAplicacao erro) {
            if (erro.categoria() == ErroAplicacao.CategoriaEnum.ACESSO_NEGADO) {
                return false;
            }
            throw erro;
        }
    }

    private void exigirPermissao(Turma registro, boolean escrita) {
        if (!autorizacao.usuario().secretaria()) {
            autorizacao.exigirTurma(registro.id(), escrita);
        }
    }
}
