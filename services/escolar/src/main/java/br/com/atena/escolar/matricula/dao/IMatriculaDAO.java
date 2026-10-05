package br.com.atena.escolar.matricula.dao;

import br.com.atena.escolar.matricula.dominio.Matricula;

import java.util.List;
import java.util.Optional;

/** Porta de persistência: isola domínio e casos de uso do Spring Data/JPA. */
public interface IMatriculaDAO {

    List<Matricula> listar();

    Optional<Matricula> buscar(Long identificador);

    Matricula salvar(Matricula registro);

    void excluir(Long identificador);

    List<Matricula> daTurma(Long turmaId);

    List<Matricula> doAluno(Long alunoId);

    boolean alunoNaTurma(Long turmaId, Long alunoId);

    List<Matricula> paginaDaTurma(Long turmaId, int pagina, int tamanho);
}
