package br.com.atena.escolar.faculdade.dao;

import br.com.atena.escolar.faculdade.dominio.Faculdade;

import java.util.List;
import java.util.Optional;

public interface IFaculdadeDAO {
    List<Faculdade> listar();

    Optional<Faculdade> buscar(Long identificador);

    Faculdade salvar(Faculdade registro);

    void excluir(Long identificador);
}
