package br.com.atena.escolar.evento.dominio;

import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.comum.dominio.ValidacoesDominio;

import java.time.LocalDate;

/** Estado escolar imutável; persistência e transporte pertencem aos adaptadores. */
public record Evento(Long id, String titulo, LocalDate inicio, LocalDate fim, String descricao) {

    public Evento {
        ValidacoesDominio.textoObrigatorio(titulo, "titulo");
        ValidacoesDominio.tamanho(titulo, 150, "titulo");
        ValidacoesDominio.obrigatorio(inicio, "inicio");
        ValidacoesDominio.obrigatorio(fim, "fim");
        if (fim.isBefore(inicio))
            throw ErroAplicacao.entrada("Fim do evento não pode ser anterior ao início");
        ValidacoesDominio.tamanho(descricao, 4000, "descricao");
    }
}
