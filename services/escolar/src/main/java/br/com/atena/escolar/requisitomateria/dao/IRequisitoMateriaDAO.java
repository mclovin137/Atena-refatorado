package br.com.atena.escolar.requisitomateria.dao;

import br.com.atena.escolar.requisitomateria.dominio.RequisitoMateria;

import java.util.List;

public interface IRequisitoMateriaDAO {
    List<RequisitoMateria> listar(Long identificador);

    void inserir(RequisitoMateria associacao);

    boolean excluir(RequisitoMateria associacao);
}
