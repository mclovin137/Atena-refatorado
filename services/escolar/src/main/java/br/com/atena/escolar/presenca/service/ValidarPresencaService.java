package br.com.atena.escolar.presenca.service;

import br.com.atena.escolar.comum.dominio.ErroAplicacao;

public final class ValidarPresencaService {
    private ValidarPresencaService() {}

    public static void validar(int faltas, int horas) {
        if (faltas > horas) throw ErroAplicacao.entrada("Faltas não podem exceder horas da aula");
    }
}
