package br.com.atena.relatorios.exportacao.helper;

public final class CampoCsvHelper {
    private CampoCsvHelper() {}

    public static String escapar(String conteudo) {
        String protegido = conteudo;
        if (!conteudo.isEmpty() && "=+-@\t\r".indexOf(conteudo.charAt(0)) >= 0) {
            protegido = "'" + conteudo;
        }
        return "\"" + protegido.replace("\"", "\"\"") + "\"";
    }
}
