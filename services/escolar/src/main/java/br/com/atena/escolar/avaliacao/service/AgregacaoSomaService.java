package br.com.atena.escolar.avaliacao.service;

import br.com.atena.escolar.avaliacao.dominio.IAgregacaoNotas;

import java.util.List;

final class AgregacaoSomaService implements IAgregacaoNotas {
    @Override
    public double calcular(List<Double> valores) {
        return Math.min(100, valores.stream().mapToDouble(Double::doubleValue).sum());
    }
}
