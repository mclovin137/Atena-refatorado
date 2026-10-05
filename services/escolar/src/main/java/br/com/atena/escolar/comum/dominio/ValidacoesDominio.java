package br.com.atena.escolar.comum.dominio;

/** Invariantes estruturais compartilhadas por estados criados em HTTP e em Batch. */
public final class ValidacoesDominio {

    private ValidacoesDominio() {}

    public static void obrigatorio(Object valor, String campo) {
        if (valor == null) {
            throw ErroAplicacao.entrada(campo + " é obrigatório");
        }
    }

    public static void textoObrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw ErroAplicacao.entrada(campo + " é obrigatório");
        }
    }

    public static void tamanho(String valor, int maximo, String campo) {
        if (valor != null && valor.length() > maximo) {
            throw ErroAplicacao.entrada(campo + " excede o tamanho permitido");
        }
    }

    public static void intervalo(Number valor, double minimo, double maximo, String campo) {
        if (valor != null
                && (!Double.isFinite(valor.doubleValue())
                        || valor.doubleValue() < minimo
                        || valor.doubleValue() > maximo)) {
            throw ErroAplicacao.entrada(campo + " fora do intervalo permitido");
        }
    }
}
