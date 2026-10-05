package br.com.atena.escolar.mensagem.dominio;

import br.com.atena.escolar.comum.dominio.ValidacoesDominio;

import java.time.LocalDateTime;

/** Estado escolar imutável; persistência e transporte pertencem aos adaptadores. */
public record Mensagem(
        Long id, Long remetenteId, Long destinatarioId, String texto, LocalDateTime enviadaEm) {

    public Mensagem {
        ValidacoesDominio.obrigatorio(destinatarioId, "destinatarioId");
        ValidacoesDominio.textoObrigatorio(texto, "texto");
        ValidacoesDominio.tamanho(texto, 4000, "texto");
    }
}
