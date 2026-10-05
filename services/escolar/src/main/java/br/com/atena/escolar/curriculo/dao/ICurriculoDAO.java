package br.com.atena.escolar.curriculo.dao;

import br.com.atena.escolar.curriculo.dominio.Curriculo;

import java.util.List;
import java.util.Optional;

/** Porta de persistência: isola domínio e casos de uso do Spring Data/JPA. */
public interface ICurriculoDAO {

    List<Curriculo> listar();

    Optional<Curriculo> buscar(Long identificador);

    Curriculo salvar(Curriculo registro);

    void excluir(Long identificador);
}
