package br.com.atena.escolar.avaliacao.service;

import br.com.atena.escolar.avaliacao.dominio.IAgregacaoNotas;
import br.com.atena.escolar.avaliacao.dominio.ModoConsolidacaoEnum;

public final class AgregacaoNotasFactory {
    public IAgregacaoNotas selecionar(ModoConsolidacaoEnum modo) {
        return switch (modo) {
            case SOMA -> new AgregacaoSomaService();
            case MEDIA -> new AgregacaoMediaService();
        };
    }
}
