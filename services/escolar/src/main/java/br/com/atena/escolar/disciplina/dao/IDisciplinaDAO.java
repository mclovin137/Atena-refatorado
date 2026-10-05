package br.com.atena.escolar.disciplina.dao;

import br.com.atena.escolar.disciplina.dominio.Disciplina;

import java.util.List;
import java.util.Optional;

/** Porta de persistência: isola domínio e casos de uso do Spring Data/JPA. */
public interface IDisciplinaDAO {

    List<Disciplina> listar();

    Optional<Disciplina> buscar(Long identificador);

    Disciplina salvar(Disciplina registro);

    void excluir(Long identificador);

    List<Disciplina> daTurma(Long turmaId);
}
