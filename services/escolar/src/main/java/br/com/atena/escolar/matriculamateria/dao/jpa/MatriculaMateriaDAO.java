package br.com.atena.escolar.matriculamateria.dao.jpa;

import br.com.atena.escolar.comum.dao.helper.ErroPersistenciaHelper;
import br.com.atena.escolar.matriculamateria.dao.IMatriculaMateriaDAO;
import br.com.atena.escolar.matriculamateria.dominio.MatriculaMateria;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MatriculaMateriaDAO implements IMatriculaMateriaDAO {
    private final IMatriculaMateriaRepositorioJpa repositorio;

    public MatriculaMateriaDAO(IMatriculaMateriaRepositorioJpa repositorio) {
        this.repositorio = repositorio;
    }

    public List<MatriculaMateria> listar(Long identificador) {
        return ErroPersistenciaHelper.executar(
                () ->
                        repositorio.listar(identificador).stream()
                                .map(
                                        a ->
                                                new MatriculaMateria(
                                                        a.chave.matriculaId, a.chave.disciplinaId))
                                .toList());
    }

    public void inserir(MatriculaMateria associacao) {
        ErroPersistenciaHelper.executar(
                () -> repositorio.inserir(associacao.matriculaId(), associacao.disciplinaId()));
    }

    public boolean excluir(MatriculaMateria associacao) {
        return ErroPersistenciaHelper.executar(
                () -> repositorio.excluir(associacao.matriculaId(), associacao.disciplinaId()) > 0);
    }
}
