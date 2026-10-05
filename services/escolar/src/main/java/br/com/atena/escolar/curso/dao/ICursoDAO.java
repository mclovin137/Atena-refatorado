package br.com.atena.escolar.curso.dao;

import br.com.atena.escolar.curso.dominio.Curso;

import java.util.List;
import java.util.Optional;

/** Porta de persistência: isola domínio e casos de uso do Spring Data/JPA. */
public interface ICursoDAO {

    List<Curso> listar();

    Optional<Curso> buscar(Long identificador);

    Curso salvar(Curso registro);

    void excluir(Long identificador);
}
