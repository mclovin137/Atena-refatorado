package br.com.atena.escolar.evento.actor;

import br.com.atena.escolar.autorizacao.actor.AutorizacaoEnsinoActor;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.evento.builder.EventoBuilder;
import br.com.atena.escolar.evento.dao.IEventoDAO;
import br.com.atena.escolar.evento.dominio.Evento;
import br.com.atena.escolar.evento.dto.EventoRequisicaoDTO;
import br.com.atena.escolar.evento.dto.EventoRespostaDTO;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ManterEventoActor {

    private final IEventoDAO registros;
    private final AutorizacaoEnsinoActor autorizacao;

    public ManterEventoActor(IEventoDAO registros, AutorizacaoEnsinoActor autorizacao) {
        this.registros = registros;
        this.autorizacao = autorizacao;
    }

    public List<EventoRespostaDTO> listar() {
        autorizacao.usuario();
        return registros.listar().stream()
                .filter(this::podeLer)
                .map(EventoBuilder::resposta)
                .toList();
    }

    public EventoRespostaDTO consultar(Long identificador) {
        var registro = buscar(identificador);
        exigirPermissao(registro, false);
        return EventoBuilder.resposta(registro);
    }

    @Transactional
    public EventoRespostaDTO criar(EventoRequisicaoDTO requisicao) {
        if (requisicao.id != null) {
            throw ErroAplicacao.entrada("Não informe id ao criar");
        }
        var registro = EventoBuilder.dominio(requisicao, null);
        exigirPermissao(registro, true);
        validarVinculos(registro);
        return EventoBuilder.resposta(registros.salvar(registro));
    }

    @Transactional
    public EventoRespostaDTO atualizar(Long identificador, EventoRequisicaoDTO requisicao) {
        exigirPermissao(buscar(identificador), true);
        var registro = EventoBuilder.dominio(requisicao, identificador);
        exigirPermissao(registro, true);
        validarVinculos(registro);
        return EventoBuilder.resposta(registros.salvar(registro));
    }

    @Transactional
    public void excluir(Long identificador) {
        exigirPermissao(buscar(identificador), true);
        registros.excluir(identificador);
    }

    private Evento buscar(Long identificador) {
        return registros
                .buscar(identificador)
                .orElseThrow(() -> ErroAplicacao.ausente("Registro não encontrado"));
    }

    private boolean podeLer(Evento registro) {
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

    private void exigirPermissao(Evento registro, boolean escrita) {
        autorizacao.usuario();
        if (escrita) {
            autorizacao.exigirSecretaria();
        }
    }

    private void validarVinculos(Evento registro) {}
}
