package br.com.atena.escolar.unidade.dao;

import br.com.atena.escolar.unidade.dominio.Unidade;

import java.util.List;
import java.util.Optional;

/** Porta de persistência: isola domínio e casos de uso do Spring Data/JPA. */
public interface IUnidadeDAO {

    List<Unidade> listar();

    Optional<Unidade> buscar(Long identificador);

    Unidade salvar(Unidade registro);

    void excluir(Long identificador);
}
