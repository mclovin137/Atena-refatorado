package br.com.atena.escolar.relatorio.dominio;

/** Preserva o resultado público da integração sem propagar exceções do cliente HTTP. */
public class ErroIntegracaoRelatorio extends RuntimeException {
    private final int codigoResposta;

    public ErroIntegracaoRelatorio(int codigoResposta, String mensagem, Throwable causa) {
        super(mensagem, causa);
        this.codigoResposta = codigoResposta;
    }

    public int codigoResposta() {
        return codigoResposta;
    }
}
