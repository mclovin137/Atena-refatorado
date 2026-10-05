package br.com.atena.escolar.horario.dao;

import br.com.atena.escolar.horario.dominio.Horario;

import java.util.List;
import java.util.Optional;

/** Porta de persistência: isola domínio e casos de uso do Spring Data/JPA. */
public interface IHorarioDAO {

    List<Horario> listar();

    Optional<Horario> buscar(Long identificador);

    Horario salvar(Horario registro);

    void excluir(Long identificador);
}
