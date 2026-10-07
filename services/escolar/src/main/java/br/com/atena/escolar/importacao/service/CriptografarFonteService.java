package br.com.atena.escolar.importacao.service;

import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.importacao.dominio.FonteCriptografada;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/** AES-GCM do JDK; não gera nem substitui a chave operacional. */
public final class CriptografarFonteService {
    private static final int VERSAO = 1;
    private final SecureRandom aleatorio = new SecureRandom();
    private final SecretKeySpec chave;

    public CriptografarFonteService(String chaveBase64) {
        byte[] bytes;
        try {
            bytes = Base64.getDecoder().decode(chaveBase64 == null ? "" : chaveBase64);
        } catch (IllegalArgumentException erro) {
            throw configuracaoInvalida();
        }
        if (bytes.length != 32) {
            throw configuracaoInvalida();
        }
        chave = new SecretKeySpec(bytes, "AES");
        java.util.Arrays.fill(bytes, (byte) 0);
    }

    public FonteCriptografada criptografar(UUID identificador, byte[] fonte) {
        var nonce = new byte[12];
        aleatorio.nextBytes(nonce);
        String digest = digest(fonte);
        try {
            var cipher = cipher(Cipher.ENCRYPT_MODE, nonce);
            cipher.updateAAD(aad(VERSAO, identificador, digest));
            return new FonteCriptografada(
                    VERSAO, identificador, digest, nonce, cipher.doFinal(fonte));
        } catch (GeneralSecurityException erro) {
            throw falhaAutenticacao();
        }
    }

    public byte[] descriptografar(FonteCriptografada fonte) {
        if (fonte.versao() != VERSAO || fonte.nonce().length != 12) {
            throw falhaAutenticacao();
        }
        try {
            var cipher = cipher(Cipher.DECRYPT_MODE, fonte.nonce());
            cipher.updateAAD(aad(fonte.versao(), fonte.identificador(), fonte.digest()));
            byte[] plaintext = cipher.doFinal(fonte.conteudo());
            if (!MessageDigest.isEqual(
                    digest(plaintext).getBytes(StandardCharsets.US_ASCII),
                    fonte.digest().getBytes(StandardCharsets.US_ASCII))) {
                java.util.Arrays.fill(plaintext, (byte) 0);
                throw falhaAutenticacao();
            }
            return plaintext;
        } catch (GeneralSecurityException erro) {
            throw falhaAutenticacao();
        }
    }

    public static String digest(byte[] fonte) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(fonte));
        } catch (GeneralSecurityException erro) {
            throw new IllegalStateException("SHA-256 indisponível");
        }
    }

    private Cipher cipher(int modo, byte[] nonce) throws GeneralSecurityException {
        var cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(modo, chave, new GCMParameterSpec(128, nonce));
        return cipher;
    }

    private byte[] aad(int versao, UUID identificador, String digest) {
        return (versao + "\n" + identificador + "\n" + digest).getBytes(StandardCharsets.UTF_8);
    }

    private static ErroAplicacao configuracaoInvalida() {
        return ErroAplicacao.conflito("Chave de importação não configurada corretamente");
    }

    private static ErroAplicacao falhaAutenticacao() {
        return ErroAplicacao.conflito("Fonte privada não pôde ser autenticada");
    }
}
