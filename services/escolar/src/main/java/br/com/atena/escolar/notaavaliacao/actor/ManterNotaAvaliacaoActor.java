package br.com.atena.escolar.notaavaliacao.actor;

import br.com.atena.escolar.autorizacao.actor.AutorizacaoEnsinoActor;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.notaavaliacao.builder.NotaAvaliacaoBuilder;
import br.com.atena.escolar.notaavaliacao.dao.INotaAvaliacaoDAO;
import br.com.atena.escolar.notaavaliacao.dominio.NotaAvaliacao;
import br.com.atena.escolar.notaavaliacao.dto.NotaAvaliacaoRequisicaoDTO;
import br.com.atena.escolar.notaavaliacao.dto.NotaAvaliacaoRespostaDTO;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ManterNotaAvaliacaoActor {

    private final INotaAvaliacaoDAO registros;
    private final AutorizacaoEnsinoActor autorizacao;
    private final AutorizacaoNotaAvaliacaoActor politica;

    public ManterNotaAvaliacaoActor(
            INotaAvaliacaoDAO registros,
            AutorizacaoEnsinoActor autorizacao,
            AutorizacaoNotaAvaliacaoActor politica) {
        this.registros = registros;
        this.autorizacao = autorizacao;
        this.politica = politica;
    }

    public List<NotaAvaliacaoRespostaDTO> listar() {
        autorizacao.usuario();
        return registros.listar().stream()
                .filter(this::podeLer)
                .map(NotaAvaliacaoBuilder::resposta)
                .toList();
    }

    public NotaAvaliacaoRespostaDTO consultar(Long identificador) {
        var registro = buscar(identificador);
        exigirPermissao(registro, false);
        return NotaAvaliacaoBuilder.resposta(registro);
    }

    @Transactional
    public NotaAvaliacaoRespostaDTO criar(NotaAvaliacaoRequisicaoDTO requisicao) {
        if (requisicao.id != null) {
            throw ErroAplicacao.entrada("Não informe id ao criar");
        }
        var registro = NotaAvaliacaoBuilder.dominio(requisicao, null);
        exigirPermissao(registro, true);
        validarVinculos(registro);
        return NotaAvaliacaoBuilder.resposta(registros.salvar(registro));
    }

    @Transactional
    public NotaAvaliacaoRespostaDTO atualizar(
            Long identificador, NotaAvaliacaoRequisicaoDTO requisicao) {
        exigirPermissao(buscar(identificador), true);
        var registro = NotaAvaliacaoBuilder.dominio(requisicao, identificador);
        exigirPermissao(registro, true);
        validarVinculos(registro);
        return NotaAvaliacaoBuilder.resposta(registros.salvar(registro));
    }

    @Transactional
    public void excluir(Long identificador) {
        exigirPermissao(buscar(identificador), true);
        registros.excluir(identificador);
    }

    private NotaAvaliacao buscar(Long identificador) {
        return registros
                .buscar(identificador)
                .orElseThrow(() -> ErroAplicacao.ausente("Registro não encontrado"));
    }

    private boolean podeLer(NotaAvaliacao registro) {
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

    private void exigirPermissao(NotaAvaliacao registro, boolean escrita) {
        politica.exigir(registro, escrita);
    }

    private void validarVinculos(NotaAvaliacao registro) {
        politica.validarVinculos(registro);
    }
}
