package br.com.atena.escolar.unidade.actor;

import br.com.atena.escolar.autorizacao.actor.AutorizacaoEnsinoActor;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.unidade.builder.UnidadeBuilder;
import br.com.atena.escolar.unidade.dao.IUnidadeDAO;
import br.com.atena.escolar.unidade.dominio.Unidade;
import br.com.atena.escolar.unidade.dto.UnidadeRequisicaoDTO;
import br.com.atena.escolar.unidade.dto.UnidadeRespostaDTO;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ManterUnidadeActor {

    private final IUnidadeDAO registros;
    private final AutorizacaoEnsinoActor autorizacao;

    public ManterUnidadeActor(IUnidadeDAO registros, AutorizacaoEnsinoActor autorizacao) {
        this.registros = registros;
        this.autorizacao = autorizacao;
    }

    public List<UnidadeRespostaDTO> listar() {
        autorizacao.usuario();
        return registros.listar().stream()
                .filter(this::podeLer)
                .map(UnidadeBuilder::resposta)
                .toList();
    }

    public UnidadeRespostaDTO consultar(Long identificador) {
        var registro = buscar(identificador);
        exigirPermissao(registro, false);
        return UnidadeBuilder.resposta(registro);
    }

    @Transactional
    public UnidadeRespostaDTO criar(UnidadeRequisicaoDTO requisicao) {
        if (requisicao.id != null) {
            throw ErroAplicacao.entrada("Não informe id ao criar");
        }
        var registro = UnidadeBuilder.dominio(requisicao, null);
        exigirPermissao(registro, true);
        return UnidadeBuilder.resposta(registros.salvar(registro));
    }

    @Transactional
    public UnidadeRespostaDTO atualizar(Long identificador, UnidadeRequisicaoDTO requisicao) {
        exigirPermissao(buscar(identificador), true);
        var registro = UnidadeBuilder.dominio(requisicao, identificador);
        exigirPermissao(registro, true);
        return UnidadeBuilder.resposta(registros.salvar(registro));
    }

    @Transactional
    public void excluir(Long identificador) {
        exigirPermissao(buscar(identificador), true);
        registros.excluir(identificador);
    }

    private Unidade buscar(Long identificador) {
        return registros
                .buscar(identificador)
                .orElseThrow(() -> ErroAplicacao.ausente("Registro não encontrado"));
    }

    private boolean podeLer(Unidade registro) {
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

    private void exigirPermissao(Unidade registro, boolean escrita) {
        autorizacao.usuario();
        if (escrita) {
            autorizacao.exigirSecretaria();
        }
    }
}
