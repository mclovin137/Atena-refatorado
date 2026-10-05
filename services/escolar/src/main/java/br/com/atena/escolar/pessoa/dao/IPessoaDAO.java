package br.com.atena.escolar.pessoa.dao;

import br.com.atena.escolar.pessoa.dominio.Pessoa;

import java.util.List;
import java.util.Optional;

/** Porta de persistência: isola domínio e casos de uso do Spring Data/JPA. */
public interface IPessoaDAO {

    List<Pessoa> listar();

    Optional<Pessoa> buscar(Long identificador);

    Pessoa salvar(Pessoa registro);

    void excluir(Long identificador);

    Optional<Pessoa> buscarPorEmail(String email);

    byte[] lerConteudo(Long identificador);

    void gravarConteudo(Long identificador, byte[] conteudo);
}
