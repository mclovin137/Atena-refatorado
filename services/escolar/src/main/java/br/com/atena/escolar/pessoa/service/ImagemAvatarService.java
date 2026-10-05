package br.com.atena.escolar.pessoa.service;

import br.com.atena.escolar.comum.dominio.ErroAplicacao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

/** Transformação determinística em memória; não conhece filesystem, sessão ou banco. */
public final class ImagemAvatarService {
    public byte[] normalizar(String nomeOriginal, byte[] bytes) {
        if (bytes.length == 0 || bytes.length > 5 * 1024 * 1024) {
            throw ErroAplicacao.entrada("Imagem deve ter até 5 MB");
        }
        String nome = nomeOriginal == null ? "" : nomeOriginal.toLowerCase(Locale.ROOT);
        if (!nome.endsWith(".png") && !nome.endsWith(".jpg") && !nome.endsWith(".jpeg")) {
            throw ErroAplicacao.entrada("Use PNG ou JPEG");
        }
        try (var entrada = new MemoryCacheImageInputStream(new ByteArrayInputStream(bytes))) {
            var leitores = ImageIO.getImageReaders(entrada);
            if (!leitores.hasNext()) {
                throw ErroAplicacao.entrada("Imagem inválida");
            }
            var leitor = leitores.next();
            try {
                leitor.setInput(entrada);
                if (!Set.of("png", "jpeg", "jpg")
                                .contains(leitor.getFormatName().toLowerCase(Locale.ROOT))
                        || leitor.getWidth(0) > 4096
                        || leitor.getHeight(0) > 4096) {
                    throw ErroAplicacao.entrada("Imagem PNG/JPEG inválida ou dimensões excedidas");
                }
                var imagem = leitor.read(0);
                var saida = new ByteArrayOutputStream();
                try (var imagemSaida = new MemoryCacheImageOutputStream(saida)) {
                    ImageIO.write(imagem, "png", imagemSaida);
                    imagemSaida.flush();
                }
                return saida.toByteArray();
            } finally {
                leitor.dispose();
            }
        } catch (IOException erro) {
            throw new ErroAplicacao(
                    ErroAplicacao.CategoriaEnum.ENTRADA_INVALIDA, "Imagem inválida", erro);
        }
    }
}
