package br.com.atena.escolar.comum.dao.helper;

import br.com.atena.escolar.comum.dominio.ErroAplicacao;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.function.Supplier;

/** Tradução na fronteira de persistência; não define CRUD ou regra de negócio. */
public final class ErroPersistenciaHelper {
    private ErroPersistenciaHelper() {}

    public static <T> T executar(Supplier<T> operacao) {
        try {
            return operacao.get();
        } catch (DataIntegrityViolationException erro) {
            throw new ErroAplicacao(
                    ErroAplicacao.CategoriaEnum.CONFLITO,
                    "Registro duplicado ou vínculo impede a operação",
                    erro);
        } catch (DataAccessException erro) {
            throw new ErroAplicacao(
                    ErroAplicacao.CategoriaEnum.FALHA_INTERNA, "Falha ao acessar os dados", erro);
        }
    }
}
