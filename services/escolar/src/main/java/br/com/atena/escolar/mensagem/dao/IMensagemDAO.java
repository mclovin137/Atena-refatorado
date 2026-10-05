package br.com.atena.escolar.mensagem.dao;

import br.com.atena.escolar.mensagem.dominio.Mensagem;

import java.util.List;
import java.util.Optional;

/** Porta de persistência: isola domínio e casos de uso do Spring Data/JPA. */
public interface IMensagemDAO {

    List<Mensagem> listar();

    Optional<Mensagem> buscar(Long identificador);

    Mensagem salvar(Mensagem registro);

    void excluir(Long identificador);
}
