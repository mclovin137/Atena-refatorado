package br.com.atena.escolar.matriculamateria.dao;

import br.com.atena.escolar.matriculamateria.dominio.MatriculaMateria;

import java.util.List;

public interface IMatriculaMateriaDAO {
    List<MatriculaMateria> listar(Long identificador);

    void inserir(MatriculaMateria associacao);

    boolean excluir(MatriculaMateria associacao);
}
