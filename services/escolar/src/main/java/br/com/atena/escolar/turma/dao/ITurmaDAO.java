package br.com.atena.escolar.turma.dao;

import br.com.atena.escolar.turma.dominio.Turma;

import java.util.List;
import java.util.Optional;

/** Porta de persistência: isola domínio e casos de uso do Spring Data/JPA. */
public interface ITurmaDAO {

    List<Turma> listar();

    Optional<Turma> buscar(Long identificador);

    Turma salvar(Turma registro);

    void excluir(Long identificador);
}
