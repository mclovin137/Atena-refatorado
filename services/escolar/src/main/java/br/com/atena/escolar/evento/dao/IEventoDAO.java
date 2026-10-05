package br.com.atena.escolar.evento.dao;

import br.com.atena.escolar.evento.dominio.Evento;

import java.util.List;
import java.util.Optional;

/** Porta de persistência: isola domínio e casos de uso do Spring Data/JPA. */
public interface IEventoDAO {

    List<Evento> listar();

    Optional<Evento> buscar(Long identificador);

    Evento salvar(Evento registro);

    void excluir(Long identificador);
}
