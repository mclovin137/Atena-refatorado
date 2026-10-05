package br.com.atena.escolar.avaliacao.dominio;

import java.util.List;

/** Duas operações qualitativas concretas que chegam ao mesmo boletim. */
public interface IAgregacaoNotas {
    double calcular(List<Double> valores);
}
