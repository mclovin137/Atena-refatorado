package br.com.atena.escolar.importacao.dominio;

public enum EstadoImportacaoEnum {
    ANALISANDO,
    PLANEJADA,
    APLICANDO,
    REPROCESSANDO,
    CONCLUIDA,
    CONCLUIDA_COM_PENDENCIAS,
    FAILED
}
