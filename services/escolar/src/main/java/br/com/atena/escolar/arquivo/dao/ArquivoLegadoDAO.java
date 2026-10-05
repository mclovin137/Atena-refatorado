package br.com.atena.escolar.arquivo.dao;

import br.com.atena.escolar.arquivo.dominio.IArquivoLegadoDAO;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Repository
public class ArquivoLegadoDAO implements IArquivoLegadoDAO {
    private final Path diretorio;

    public ArquivoLegadoDAO(@Value("${UPLOAD_DIR:/data/uploads}") String diretorio) {
        this.diretorio = Path.of(diretorio).toAbsolutePath().normalize();
    }

    private Path caminho(String identificador) {
        Path arquivo = diretorio.resolve(identificador).normalize();
        if (!arquivo.startsWith(diretorio) || arquivo.equals(diretorio)) {
            throw ErroAplicacao.entrada("Referência de arquivo inválida");
        }
        return arquivo;
    }

    @Override
    public byte[] ler(String identificador) {
        try {
            return Files.readAllBytes(caminho(identificador));
        } catch (IOException erro) {
            throw new ErroAplicacao(
                    ErroAplicacao.CategoriaEnum.FALHA_INTERNA, "Arquivo legado indisponível", erro);
        }
    }

    @Override
    public void excluir(String identificador) {
        if (identificador == null) {
            return;
        }
        try {
            Files.deleteIfExists(caminho(identificador));
        } catch (IOException erro) {
            throw new ErroAplicacao(
                    ErroAplicacao.CategoriaEnum.FALHA_INTERNA,
                    "Não foi possível remover o arquivo",
                    erro);
        }
    }
}
