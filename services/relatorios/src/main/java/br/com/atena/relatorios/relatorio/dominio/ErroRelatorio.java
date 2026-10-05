package br.com.atena.relatorios.relatorio.dominio;

public class ErroRelatorio extends RuntimeException {
    public enum CategoriaEnum {
        INVALIDO,
        NAO_ENCONTRADO,
        NAO_CONCLUIDO,
        CONFLITO
    }

    private final CategoriaEnum categoria;

    public ErroRelatorio(CategoriaEnum categoria, String mensagem) {
        super(mensagem);
        this.categoria = categoria;
    }

    public ErroRelatorio(CategoriaEnum categoria, String mensagem, Throwable causa) {
        super(mensagem, causa);
        this.categoria = categoria;
    }

    public CategoriaEnum categoria() {
        return categoria;
    }
}
