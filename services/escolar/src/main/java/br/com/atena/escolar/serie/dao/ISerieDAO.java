package br.com.atena.escolar.serie.dao;

import br.com.atena.escolar.serie.dominio.Serie;

import java.util.List;
import java.util.Optional;

/** Porta de persistência: isola domínio e casos de uso do Spring Data/JPA. */
public interface ISerieDAO {

    List<Serie> listar();

    Optional<Serie> buscar(Long identificador);

    Serie salvar(Serie registro);

    void excluir(Long identificador);
}
