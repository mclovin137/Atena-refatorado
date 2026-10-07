package br.com.atena.escolar.importacao.actor;

import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.importacao.dao.IFonteImportacaoDAO;
import br.com.atena.escolar.importacao.dominio.RegistroLegado;
import br.com.atena.escolar.importacao.service.CriptografarFonteService;
import br.com.atena.escolar.importacao.service.LerDumpLegadoService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class PrepararFonteImportacaoActor {
    private final IFonteImportacaoDAO fontes;
    private final String chave;

    public PrepararFonteImportacaoActor(
            IFonteImportacaoDAO fontes, @Value("${IMPORT_ENCRYPTION_KEY:}") String chave) {
        this.fontes = fontes;
        this.chave = chave;
    }

    public UUID guardar(UUID id, byte[] conteudo) {
        var crypto = new CriptografarFonteService(chave);
        var existente = fontes.buscarDigest(CriptografarFonteService.digest(conteudo));
        if (existente.isPresent()) {
            return existente.get();
        }
        return fontes.guardar(crypto.criptografar(id, conteudo));
    }

    public List<RegistroLegado> ler(UUID id) {
        byte[] plaintext = new CriptografarFonteService(chave).descriptografar(fontes.fonte(id));
        try {
            var decoder =
                    StandardCharsets.UTF_8
                            .newDecoder()
                            .onMalformedInput(CodingErrorAction.REPORT)
                            .onUnmappableCharacter(CodingErrorAction.REPORT);
            return new LerDumpLegadoService()
                    .executar(decoder.decode(ByteBuffer.wrap(plaintext)).toString());
        } catch (CharacterCodingException erro) {
            throw ErroAplicacao.entrada("Arquivo legado deve usar UTF-8 válido");
        } finally {
            Arrays.fill(plaintext, (byte) 0);
        }
    }
}
