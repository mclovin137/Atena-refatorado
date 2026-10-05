package br.com.atena.escolar.nota.dao;

import br.com.atena.escolar.nota.dominio.Nota;

import java.util.List;
import java.util.Optional;

/** Porta de persistência: isola domínio e casos de uso do Spring Data/JPA. */
public interface INotaDAO {

    List<Nota> listar();

    Optional<Nota> buscar(Long identificador);

    Nota salvar(Nota registro);

    void excluir(Long identificador);

    List<Nota> doAlunoNaDisciplina(Long disciplinaId, Long alunoId);

    boolean existePeriodo(Long disciplinaId, Long alunoId, int bimestre);

    void salvarAusente(Nota registro);
}
