package br.com.atena.escolar.horario.dominio;

import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.comum.dominio.ValidacoesDominio;

import java.time.LocalTime;

/** Estado escolar imutável; persistência e transporte pertencem aos adaptadores. */
public record Horario(Long id, Long disciplinaId, int diaSemana, LocalTime inicio, LocalTime fim) {

    public Horario {
        ValidacoesDominio.obrigatorio(disciplinaId, "disciplinaId");
        ValidacoesDominio.intervalo(diaSemana, 1, 7, "diaSemana");
        ValidacoesDominio.obrigatorio(inicio, "inicio");
        ValidacoesDominio.obrigatorio(fim, "fim");
        if (!fim.isAfter(inicio))
            throw ErroAplicacao.entrada("Fim do horário deve ser posterior ao início");
    }
}
