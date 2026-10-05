package br.com.atena.escolar.serie.actor;

import br.com.atena.escolar.autorizacao.actor.AutorizacaoEnsinoActor;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.serie.builder.SerieBuilder;
import br.com.atena.escolar.serie.dao.ISerieDAO;
import br.com.atena.escolar.serie.dominio.Serie;
import br.com.atena.escolar.serie.dto.SerieRequisicaoDTO;
import br.com.atena.escolar.serie.dto.SerieRespostaDTO;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ManterSerieActor {

    private final ISerieDAO registros;
    private final AutorizacaoEnsinoActor autorizacao;

    public ManterSerieActor(ISerieDAO registros, AutorizacaoEnsinoActor autorizacao) {
        this.registros = registros;
        this.autorizacao = autorizacao;
    }

    public List<SerieRespostaDTO> listar() {
        autorizacao.usuario();
        return registros.listar().stream()
                .filter(this::podeLer)
                .map(SerieBuilder::resposta)
                .toList();
    }

    public SerieRespostaDTO consultar(Long identificador) {
        var registro = buscar(identificador);
        exigirPermissao(registro, false);
        return SerieBuilder.resposta(registro);
    }

    @Transactional
    public SerieRespostaDTO criar(SerieRequisicaoDTO requisicao) {
        if (requisicao.id != null) {
            throw ErroAplicacao.entrada("Não informe id ao criar");
        }
        var registro = SerieBuilder.dominio(requisicao, null);
        exigirPermissao(registro, true);
        return SerieBuilder.resposta(registros.salvar(registro));
    }

    @Transactional
    public SerieRespostaDTO atualizar(Long identificador, SerieRequisicaoDTO requisicao) {
        exigirPermissao(buscar(identificador), true);
        var registro = SerieBuilder.dominio(requisicao, identificador);
        exigirPermissao(registro, true);
        return SerieBuilder.resposta(registros.salvar(registro));
    }

    @Transactional
    public void excluir(Long identificador) {
        exigirPermissao(buscar(identificador), true);
        registros.excluir(identificador);
    }

    private Serie buscar(Long identificador) {
        return registros
                .buscar(identificador)
                .orElseThrow(() -> ErroAplicacao.ausente("Registro não encontrado"));
    }

    private boolean podeLer(Serie registro) {
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

    private void exigirPermissao(Serie registro, boolean escrita) {
        autorizacao.usuario();
        if (escrita) {
            autorizacao.exigirSecretaria();
        }
    }
}
