package br.com.atena.escolar.presenca.dao;

import br.com.atena.escolar.presenca.dominio.Presenca;

import java.util.List;
import java.util.Optional;

/** Porta de persistência: isola domínio e casos de uso do Spring Data/JPA. */
public interface IPresencaDAO {

    List<Presenca> listar();

    Optional<Presenca> buscar(Long identificador);

    Presenca salvar(Presenca registro);

    void excluir(Long identificador);

    List<Presenca> daAula(Long aulaId);
}
