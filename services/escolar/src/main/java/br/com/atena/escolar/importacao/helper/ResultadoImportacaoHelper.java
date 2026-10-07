package br.com.atena.escolar.importacao.helper;

import br.com.atena.escolar.importacao.dto.RegistroImportacaoDTO;

import java.util.Map;

/** Mantém a identidade de origem ao classificar o resultado de uma projeção. */
public final class ResultadoImportacaoHelper {
    private ResultadoImportacaoHelper() {}

    public static RegistroImportacaoDTO importado(
            RegistroImportacaoDTO origem, Map<String, Long> destinos) {
        return new RegistroImportacaoDTO(
                origem.tabela(),
                origem.ordinal(),
                origem.origemId(),
                "IMPORTADO",
                null,
                origem.dados(),
                destinos);
    }

    public static RegistroImportacaoDTO pendente(RegistroImportacaoDTO origem, String motivo) {
        return new RegistroImportacaoDTO(
                origem.tabela(),
                origem.ordinal(),
                origem.origemId(),
                "PENDENTE",
                motivo,
                origem.dados(),
                origem.destinos());
    }
}
