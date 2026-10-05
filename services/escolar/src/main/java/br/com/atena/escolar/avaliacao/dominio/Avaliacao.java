package br.com.atena.escolar.avaliacao.dominio;

import br.com.atena.escolar.comum.dominio.ValidacoesDominio;

import java.time.LocalDate;

/** Estado escolar imutável; persistência e transporte pertencem aos adaptadores. */
public record Avaliacao(
        Long id,
        Long disciplinaId,
        String nome,
        int bimestre,
        Double valor,
        LocalDate data,
        String tipo) {

    public Avaliacao {
        ValidacoesDominio.obrigatorio(disciplinaId, "disciplinaId");
        ValidacoesDominio.textoObrigatorio(nome, "nome");
        ValidacoesDominio.tamanho(nome, 150, "nome");
        ValidacoesDominio.intervalo(bimestre, 1, 4, "bimestre");
        ValidacoesDominio.obrigatorio(valor, "valor");
        ValidacoesDominio.intervalo(valor, 0.01, 100, "valor");
        ValidacoesDominio.obrigatorio(data, "data");
        ValidacoesDominio.textoObrigatorio(tipo, "tipo");
    }
}
