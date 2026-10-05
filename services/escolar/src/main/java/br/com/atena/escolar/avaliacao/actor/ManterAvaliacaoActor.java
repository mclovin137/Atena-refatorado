package br.com.atena.escolar.avaliacao.actor;

import br.com.atena.escolar.autorizacao.actor.AutorizacaoEnsinoActor;
import br.com.atena.escolar.avaliacao.builder.AvaliacaoBuilder;
import br.com.atena.escolar.avaliacao.dao.IAvaliacaoDAO;
import br.com.atena.escolar.avaliacao.dominio.Avaliacao;
import br.com.atena.escolar.avaliacao.dto.AvaliacaoRequisicaoDTO;
import br.com.atena.escolar.avaliacao.dto.AvaliacaoRespostaDTO;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ManterAvaliacaoActor {

    private final IAvaliacaoDAO registros;
    private final AutorizacaoEnsinoActor autorizacao;

    public ManterAvaliacaoActor(IAvaliacaoDAO registros, AutorizacaoEnsinoActor autorizacao) {
        this.registros = registros;
        this.autorizacao = autorizacao;
    }

    public List<AvaliacaoRespostaDTO> listar() {
        autorizacao.usuario();
        return registros.listar().stream()
                .filter(this::podeLer)
                .map(AvaliacaoBuilder::resposta)
                .toList();
    }

    public AvaliacaoRespostaDTO consultar(Long identificador) {
        var registro = buscar(identificador);
        exigirPermissao(registro, false);
        return AvaliacaoBuilder.resposta(registro);
    }

    @Transactional
    public AvaliacaoRespostaDTO criar(AvaliacaoRequisicaoDTO requisicao) {
        if (requisicao.id != null) {
            throw ErroAplicacao.entrada("Não informe id ao criar");
        }
        var registro = AvaliacaoBuilder.dominio(requisicao, null);
        exigirPermissao(registro, true);
        return AvaliacaoBuilder.resposta(registros.salvar(registro));
    }

    @Transactional
    public AvaliacaoRespostaDTO atualizar(Long identificador, AvaliacaoRequisicaoDTO requisicao) {
        exigirPermissao(buscar(identificador), true);
        var registro = AvaliacaoBuilder.dominio(requisicao, identificador);
        exigirPermissao(registro, true);
        return AvaliacaoBuilder.resposta(registros.salvar(registro));
    }

    @Transactional
    public void excluir(Long identificador) {
        exigirPermissao(buscar(identificador), true);
        registros.excluir(identificador);
    }

    private Avaliacao buscar(Long identificador) {
        return registros
                .buscar(identificador)
                .orElseThrow(() -> ErroAplicacao.ausente("Registro não encontrado"));
    }

    private boolean podeLer(Avaliacao registro) {
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

    private void exigirPermissao(Avaliacao registro, boolean escrita) {
        if (!autorizacao.usuario().secretaria()) {
            autorizacao.exigirDisciplina(registro.disciplinaId(), escrita);
        }
    }
}
