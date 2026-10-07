package br.com.atena.escolar.importacao.dominio;

import java.util.UUID;

/** Envelope autenticado imutável, persistido antes do processamento em chunks. */
public record FonteCriptografada(
        int versao, UUID identificador, String digest, byte[] nonce, byte[] conteudo) {
    public FonteCriptografada {
        nonce = nonce.clone();
        conteudo = conteudo.clone();
    }

    @Override
    public byte[] nonce() {
        return nonce.clone();
    }

    @Override
    public byte[] conteudo() {
        return conteudo.clone();
    }
}
