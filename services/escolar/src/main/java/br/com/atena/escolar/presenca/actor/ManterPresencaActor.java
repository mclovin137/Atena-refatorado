package br.com.atena.escolar.presenca.actor;

import br.com.atena.escolar.autorizacao.actor.AutorizacaoEnsinoActor;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.presenca.builder.PresencaBuilder;
import br.com.atena.escolar.presenca.dao.IPresencaDAO;
import br.com.atena.escolar.presenca.dominio.Presenca;
import br.com.atena.escolar.presenca.dto.PresencaRequisicaoDTO;
import br.com.atena.escolar.presenca.dto.PresencaRespostaDTO;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ManterPresencaActor {

    private final IPresencaDAO registros;
    private final AutorizacaoEnsinoActor autorizacao;
    private final AutorizacaoPresencaActor politica;

    public ManterPresencaActor(
            IPresencaDAO registros,
            AutorizacaoEnsinoActor autorizacao,
            AutorizacaoPresencaActor politica) {
        this.registros = registros;
        this.autorizacao = autorizacao;
        this.politica = politica;
    }

    public List<PresencaRespostaDTO> listar() {
        autorizacao.usuario();
        return registros.listar().stream()
                .filter(this::podeLer)
                .map(PresencaBuilder::resposta)
                .toList();
    }

    public PresencaRespostaDTO consultar(Long identificador) {
        var registro = buscar(identificador);
        exigirPermissao(registro, false);
        return PresencaBuilder.resposta(registro);
    }

    @Transactional
    public PresencaRespostaDTO criar(PresencaRequisicaoDTO requisicao) {
        if (requisicao.id != null) {
            throw ErroAplicacao.entrada("Não informe id ao criar");
        }
        var registro = PresencaBuilder.dominio(requisicao, null);
        exigirPermissao(registro, true);
        validarVinculos(registro);
        return PresencaBuilder.resposta(registros.salvar(registro));
    }

    @Transactional
    public PresencaRespostaDTO atualizar(Long identificador, PresencaRequisicaoDTO requisicao) {
        exigirPermissao(buscar(identificador), true);
        var registro = PresencaBuilder.dominio(requisicao, identificador);
        exigirPermissao(registro, true);
        validarVinculos(registro);
        return PresencaBuilder.resposta(registros.salvar(registro));
    }

    @Transactional
    public void excluir(Long identificador) {
        exigirPermissao(buscar(identificador), true);
        registros.excluir(identificador);
    }

    private Presenca buscar(Long identificador) {
        return registros
                .buscar(identificador)
                .orElseThrow(() -> ErroAplicacao.ausente("Registro não encontrado"));
    }

    private boolean podeLer(Presenca registro) {
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

    private void exigirPermissao(Presenca registro, boolean escrita) {
        politica.exigir(registro, escrita);
    }

    private void validarVinculos(Presenca registro) {
        politica.validarVinculos(registro);
    }
}
