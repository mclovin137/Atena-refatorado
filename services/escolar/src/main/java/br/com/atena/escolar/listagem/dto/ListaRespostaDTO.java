package br.com.atena.escolar.listagem.dto;

import java.util.List;

public record ListaRespostaDTO(String nome, List<LinhaListaDTO> linhas) {}
