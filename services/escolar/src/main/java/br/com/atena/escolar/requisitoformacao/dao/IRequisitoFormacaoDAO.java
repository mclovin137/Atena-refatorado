package br.com.atena.escolar.requisitoformacao.dao;

import br.com.atena.escolar.requisitoformacao.dominio.RequisitoFormacao;

import java.util.List;
import java.util.Optional;

public interface IRequisitoFormacaoDAO {
    List<RequisitoFormacao> listar();

    Optional<RequisitoFormacao> buscar(Long identificador);

    RequisitoFormacao salvar(RequisitoFormacao registro);

    void excluir(Long identificador);
}
