package br.com.atena.escolar.materia.dao;

import br.com.atena.escolar.materia.dominio.Materia;

import java.util.List;
import java.util.Optional;

/** Porta de persistência: isola domínio e casos de uso do Spring Data/JPA. */
public interface IMateriaDAO {

    List<Materia> listar();

    Optional<Materia> buscar(Long identificador);

    Materia salvar(Materia registro);

    void excluir(Long identificador);
}
