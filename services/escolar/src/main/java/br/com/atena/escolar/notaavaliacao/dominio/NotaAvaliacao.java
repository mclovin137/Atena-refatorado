package br.com.atena.escolar.notaavaliacao.dominio;

import br.com.atena.escolar.comum.dominio.ValidacoesDominio;

/** Estado escolar imutável; persistência e transporte pertencem aos adaptadores. */
public record NotaAvaliacao(Long id, Long avaliacaoId, Long alunoId, Double valor) {

    public NotaAvaliacao {
        ValidacoesDominio.obrigatorio(avaliacaoId, "avaliacaoId");
        ValidacoesDominio.obrigatorio(alunoId, "alunoId");
        ValidacoesDominio.obrigatorio(valor, "valor");
        ValidacoesDominio.intervalo(valor, 0, 100, "valor");
    }
}
