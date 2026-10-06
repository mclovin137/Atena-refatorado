package br.com.atena.escolar.requisitomateria.dao.jpa;

import br.com.atena.escolar.comum.dao.helper.ErroPersistenciaHelper;
import br.com.atena.escolar.requisitomateria.dao.IRequisitoMateriaDAO;
import br.com.atena.escolar.requisitomateria.dominio.RequisitoMateria;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RequisitoMateriaDAO implements IRequisitoMateriaDAO {
    private final IRequisitoMateriaRepositorioJpa repositorio;

    public RequisitoMateriaDAO(IRequisitoMateriaRepositorioJpa repositorio) {
        this.repositorio = repositorio;
    }

    public List<RequisitoMateria> listar(Long identificador) {
        return ErroPersistenciaHelper.executar(
                () ->
                        repositorio.listar(identificador).stream()
                                .map(
                                        a ->
                                                new RequisitoMateria(
                                                        a.chave.requisitoId, a.chave.materiaId))
                                .toList());
    }

    public void inserir(RequisitoMateria associacao) {
        ErroPersistenciaHelper.executar(
                () -> repositorio.inserir(associacao.requisitoId(), associacao.materiaId()));
    }

    public boolean excluir(RequisitoMateria associacao) {
        return ErroPersistenciaHelper.executar(
                () -> repositorio.excluir(associacao.requisitoId(), associacao.materiaId()) > 0);
    }
}
