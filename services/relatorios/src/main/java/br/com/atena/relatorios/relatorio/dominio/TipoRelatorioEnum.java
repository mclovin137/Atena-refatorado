package br.com.atena.relatorios.relatorio.dominio;

public enum TipoRelatorioEnum {
    HISTORICO,
    BOLETIM,
    LISTA_ALUNOS,
    LISTA_PROFESSORES,
    LISTA_TURMAS;

    public boolean lista() {
        return name().startsWith("LISTA_");
    }
}
