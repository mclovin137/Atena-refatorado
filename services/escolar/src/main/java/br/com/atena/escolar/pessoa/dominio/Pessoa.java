package br.com.atena.escolar.pessoa.dominio;

import br.com.atena.escolar.comum.dominio.ValidacoesDominio;
import br.com.atena.escolar.usuario.dominio.PerfilEnum;

import java.time.LocalDate;

/** Estado escolar imutável; persistência e transporte pertencem aos adaptadores. */
public record Pessoa(
        Long id,
        String nome,
        String email,
        String matricula,
        String perfil,
        String senhaHash,
        boolean ativo,
        String telefone,
        String endereco,
        String documento,
        LocalDate nascimento,
        String cep,
        String pai,
        String mae,
        String estadoCivil,
        String sexo,
        String telefoneResponsavel,
        String emailResponsavel,
        String avatar,
        byte[] avatarConteudo) {

    public Pessoa {
        avatarConteudo = avatarConteudo == null ? null : avatarConteudo.clone();
        PerfilEnum.valueOf(perfil);
        ValidacoesDominio.textoObrigatorio(nome, "nome");
        ValidacoesDominio.tamanho(nome, 150, "nome");
        ValidacoesDominio.textoObrigatorio(email, "email");
        ValidacoesDominio.tamanho(email, 180, "email");
        ValidacoesDominio.textoObrigatorio(matricula, "matricula");
        ValidacoesDominio.tamanho(matricula, 40, "matricula");
        ValidacoesDominio.textoObrigatorio(perfil, "perfil");
        ValidacoesDominio.tamanho(telefone, 30, "telefone");
        ValidacoesDominio.tamanho(endereco, 500, "endereco");
        ValidacoesDominio.tamanho(documento, 40, "documento");
        ValidacoesDominio.tamanho(cep, 12, "cep");
        ValidacoesDominio.tamanho(pai, 150, "pai");
        ValidacoesDominio.tamanho(mae, 150, "mae");
        ValidacoesDominio.tamanho(estadoCivil, 40, "estadoCivil");
        ValidacoesDominio.tamanho(sexo, 30, "sexo");
        ValidacoesDominio.tamanho(telefoneResponsavel, 30, "telefoneResponsavel");
        ValidacoesDominio.tamanho(emailResponsavel, 180, "emailResponsavel");
    }

    @Override
    public byte[] avatarConteudo() {
        return avatarConteudo == null ? null : avatarConteudo.clone();
    }
}
