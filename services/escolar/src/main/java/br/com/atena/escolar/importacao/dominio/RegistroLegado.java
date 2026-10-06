package br.com.atena.escolar.importacao.dominio;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** Valores literais de uma origem; nunca representa uma instrução executável. */
public record RegistroLegado(String tabela, int ordinal, Map<String, String> campos) {
    public RegistroLegado {
        campos = Collections.unmodifiableMap(new LinkedHashMap<>(campos));
    }

    public String campo(String nome) {
        return campos.get(nome);
    }
}
