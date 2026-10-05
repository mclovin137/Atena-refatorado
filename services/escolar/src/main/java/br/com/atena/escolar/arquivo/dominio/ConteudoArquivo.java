package br.com.atena.escolar.arquivo.dominio;

/** Download autorizado: não expõe localização do armazenamento. */
public record ConteudoArquivo(byte[] bytes, String tipo, String nome) {}
