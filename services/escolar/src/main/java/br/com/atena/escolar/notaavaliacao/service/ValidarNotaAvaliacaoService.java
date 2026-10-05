package br.com.atena.escolar.notaavaliacao.service;

import br.com.atena.escolar.comum.dominio.ErroAplicacao;

public final class ValidarNotaAvaliacaoService {
    private ValidarNotaAvaliacaoService() {}

    public static void validar(double nota, double limite) {
        if (nota > limite) throw ErroAplicacao.entrada("Nota excede o valor da avaliação");
    }
}
