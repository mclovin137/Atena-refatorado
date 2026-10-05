package br.com.atena.escolar.mensagem.builder;

import br.com.atena.escolar.mensagem.dominio.Mensagem;
import br.com.atena.escolar.mensagem.dto.MensagemRequisicaoDTO;
import br.com.atena.escolar.mensagem.dto.MensagemRespostaDTO;

public final class MensagemBuilder {

    private MensagemBuilder() {}

    public static Mensagem dominio(MensagemRequisicaoDTO requisicao, Long identificador) {
        return new Mensagem(
                identificador,
                requisicao.remetenteId,
                requisicao.destinatarioId,
                requisicao.texto,
                requisicao.enviadaEm);
    }

    public static MensagemRespostaDTO resposta(Mensagem registro) {
        return new MensagemRespostaDTO(
                registro.id(),
                registro.remetenteId(),
                registro.destinatarioId(),
                registro.texto(),
                registro.enviadaEm());
    }
}
