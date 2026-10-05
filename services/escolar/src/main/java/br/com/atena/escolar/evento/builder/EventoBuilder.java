package br.com.atena.escolar.evento.builder;

import br.com.atena.escolar.evento.dominio.Evento;
import br.com.atena.escolar.evento.dto.EventoRequisicaoDTO;
import br.com.atena.escolar.evento.dto.EventoRespostaDTO;

public final class EventoBuilder {

    private EventoBuilder() {}

    public static Evento dominio(EventoRequisicaoDTO requisicao, Long identificador) {
        return new Evento(
                identificador,
                requisicao.titulo,
                requisicao.inicio,
                requisicao.fim,
                requisicao.descricao);
    }

    public static EventoRespostaDTO resposta(Evento registro) {
        return new EventoRespostaDTO(
                registro.id(),
                registro.titulo(),
                registro.inicio(),
                registro.fim(),
                registro.descricao());
    }
}
