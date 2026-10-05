package br.com.atena.escolar.material.service;

import br.com.atena.escolar.comum.dominio.ErroAplicacao;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

public final class MaterialService {

    public String identificarTipo(String nomeOriginal, byte[] bytes) {
        if (bytes.length == 0 || bytes.length > 10 * 1024 * 1024) {
            throw ErroAplicacao.entrada("Arquivo ou nome inválido");
        }
        String nome = nomeOriginal == null ? "" : nomeOriginal.toLowerCase(Locale.ROOT);
        if (nome.endsWith(".pdf")
                && bytes.length >= 5
                && new String(bytes, 0, 5, StandardCharsets.US_ASCII).equals("%PDF-")) {
            return "application/pdf";
        }
        if (nome.endsWith(".png")
                && bytes.length >= 8
                && bytes[0] == (byte) 137
                && bytes[1] == 80
                && bytes[2] == 78
                && bytes[3] == 71) {
            return "image/png";
        }
        if ((nome.endsWith(".jpg") || nome.endsWith(".jpeg"))
                && bytes.length >= 3
                && bytes[0] == (byte) 255
                && bytes[1] == (byte) 216
                && bytes[2] == (byte) 255) {
            return "image/jpeg";
        }
        if (nome.endsWith(".doc")
                && bytes.length >= 8
                && bytes[0] == (byte) 0xD0
                && bytes[1] == (byte) 0xCF
                && bytes[2] == (byte) 0x11
                && bytes[3] == (byte) 0xE0) {
            return "application/msword";
        }
        if (nome.endsWith(".zip")
                && bytes.length >= 4
                && bytes[0] == 80
                && bytes[1] == 75
                && (bytes[2] == 3 || bytes[2] == 5 || bytes[2] == 7)) {
            return "application/zip";
        }
        if (nome.endsWith(".rar")
                && bytes.length >= 7
                && bytes[0] == 82
                && bytes[1] == 97
                && bytes[2] == 114
                && bytes[3] == 33
                && bytes[4] == 26
                && bytes[5] == 7) {
            return "application/vnd.rar";
        }
        throw ErroAplicacao.entrada(
                "Tipos aceitos: PDF, DOC, ZIP, RAR, PNG e JPEG com assinatura correspondente");
    }

    public String extensao(String tipo) {
        return switch (tipo) {
            case "application/pdf" -> ".pdf";
            case "image/png" -> ".png";
            case "image/jpeg" -> ".jpg";
            case "application/msword" -> ".doc";
            case "application/zip" -> ".zip";
            default -> ".rar";
        };
    }
}
