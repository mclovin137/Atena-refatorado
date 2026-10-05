package br.com.atena.escolar.nota.actor;

import br.com.atena.escolar.autorizacao.actor.AutorizacaoEnsinoActor;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.nota.builder.NotaBuilder;
import br.com.atena.escolar.nota.dao.INotaDAO;
import br.com.atena.escolar.nota.dominio.Nota;
import br.com.atena.escolar.nota.dto.NotaRequisicaoDTO;
import br.com.atena.escolar.nota.dto.NotaRespostaDTO;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ManterNotaActor {

    private final INotaDAO registros;
    private final AutorizacaoEnsinoActor autorizacao;
    private final AutorizacaoNotaActor politica;

    public ManterNotaActor(
            INotaDAO registros, AutorizacaoEnsinoActor autorizacao, AutorizacaoNotaActor politica) {
        this.registros = registros;
        this.autorizacao = autorizacao;
        this.politica = politica;
    }

    public List<NotaRespostaDTO> listar() {
        autorizacao.usuario();
        return registros.listar().stream()
                .filter(this::podeLer)
                .map(NotaBuilder::resposta)
                .toList();
    }

    public NotaRespostaDTO consultar(Long identificador) {
        var registro = buscar(identificador);
        exigirPermissao(registro, false);
        return NotaBuilder.resposta(registro);
    }

    @Transactional
    public NotaRespostaDTO criar(NotaRequisicaoDTO requisicao) {
        if (requisicao.id != null) {
            throw ErroAplicacao.entrada("Não informe id ao criar");
        }
        var registro = NotaBuilder.dominio(requisicao, null);
        exigirPermissao(registro, true);
        validarVinculos(registro);
        return NotaBuilder.resposta(registros.salvar(registro));
    }

    @Transactional
    public NotaRespostaDTO atualizar(Long identificador, NotaRequisicaoDTO requisicao) {
        exigirPermissao(buscar(identificador), true);
        var registro = NotaBuilder.dominio(requisicao, identificador);
        exigirPermissao(registro, true);
        validarVinculos(registro);
        return NotaBuilder.resposta(registros.salvar(registro));
    }

    @Transactional
    public void excluir(Long identificador) {
        autorizacao.exigirSecretaria();
        exigirPermissao(buscar(identificador), true);
        registros.excluir(identificador);
    }

    private Nota buscar(Long identificador) {
        return registros
                .buscar(identificador)
                .orElseThrow(() -> ErroAplicacao.ausente("Registro não encontrado"));
    }

    private boolean podeLer(Nota registro) {
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

    private void exigirPermissao(Nota registro, boolean escrita) {
        politica.exigir(registro, escrita);
    }

    private void validarVinculos(Nota registro) {
        politica.validarVinculos(registro);
    }
}
