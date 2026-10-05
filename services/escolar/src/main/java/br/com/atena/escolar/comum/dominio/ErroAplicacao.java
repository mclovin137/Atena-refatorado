package br.com.atena.escolar.comum.dominio;

/** Falha categorizada independente do transporte; a borda decide o status HTTP. */
public class ErroAplicacao extends RuntimeException {

    public enum CategoriaEnum {
        ENTRADA_INVALIDA,
        NAO_AUTENTICADO,
        ACESSO_NEGADO,
        NAO_ENCONTRADO,
        CONFLITO,
        FALHA_INTERNA
    }

    private final CategoriaEnum categoria;

    public ErroAplicacao(CategoriaEnum categoria, String mensagem) {
        super(mensagem);
        this.categoria = categoria;
    }

    public ErroAplicacao(CategoriaEnum categoria, String mensagem, Throwable causa) {
        super(mensagem, causa);
        this.categoria = categoria;
    }

    public CategoriaEnum categoria() {
        return categoria;
    }

    public static ErroAplicacao entrada(String mensagem) {
        return new ErroAplicacao(CategoriaEnum.ENTRADA_INVALIDA, mensagem);
    }

    public static ErroAplicacao ausente(String mensagem) {
        return new ErroAplicacao(CategoriaEnum.NAO_ENCONTRADO, mensagem);
    }

    public static ErroAplicacao conflito(String mensagem) {
        return new ErroAplicacao(CategoriaEnum.CONFLITO, mensagem);
    }

    public static void negar() {
        throw new ErroAplicacao(CategoriaEnum.ACESSO_NEGADO, "Acesso negado");
    }
}
