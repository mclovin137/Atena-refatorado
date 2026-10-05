package br.com.atena.escolar.avaliacao.service;

import br.com.atena.escolar.avaliacao.dominio.IAgregacaoNotas;

import java.util.List;

final class AgregacaoMediaService implements IAgregacaoNotas {
    @Override
    public double calcular(List<Double> valores) {
        return valores.stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }
}
