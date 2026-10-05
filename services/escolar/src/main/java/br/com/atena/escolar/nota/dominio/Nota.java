package br.com.atena.escolar.nota.dominio;

import br.com.atena.escolar.comum.dominio.ValidacoesDominio;

import java.time.LocalDate;

/** Estado escolar imutável; persistência e transporte pertencem aos adaptadores. */
public record Nota(
        Long id,
        Long disciplinaId,
        Long alunoId,
        int bimestre,
        Double valor,
        Double recuperacao,
        LocalDate prazo,
        boolean liberada) {

    public Nota {
        ValidacoesDominio.obrigatorio(disciplinaId, "disciplinaId");
        ValidacoesDominio.obrigatorio(alunoId, "alunoId");
        ValidacoesDominio.intervalo(bimestre, 1, 4, "bimestre");
        ValidacoesDominio.intervalo(valor, 0, 100, "valor");
        ValidacoesDominio.intervalo(recuperacao, 0, 60, "recuperacao");
    }
}
