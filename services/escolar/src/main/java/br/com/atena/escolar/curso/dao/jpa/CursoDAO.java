package br.com.atena.escolar.curso.dao.jpa;

import br.com.atena.escolar.comum.dao.helper.ErroPersistenciaHelper;
import br.com.atena.escolar.curso.dao.ICursoDAO;
import br.com.atena.escolar.curso.dominio.Curso;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CursoDAO implements ICursoDAO {

    private final ICursoRepositorioJpa repositorio;

    public CursoDAO(ICursoRepositorioJpa repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Curso> listar() {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findAll(Sort.by("id")).stream().map(this::dominio).toList();
                });
    }

    @Override
    public Optional<Curso> buscar(Long identificador) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findById(identificador).map(this::dominio);
                });
    }

    @Override
    public Curso salvar(Curso registro) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return dominio(repositorio.saveAndFlush(entidade(registro)));
                });
    }

    @Override
    public void excluir(Long identificador) {
        ErroPersistenciaHelper.executar(
                () -> {
                    repositorio.deleteById(identificador);
                    repositorio.flush();

                    return null;
                });
    }

    private Curso dominio(CursoEntidadeJpa entidade) {
        return new Curso(entidade.id, entidade.nome, entidade.faculdadeId);
    }

    private CursoEntidadeJpa entidade(Curso registro) {
        CursoEntidadeJpa entidade = new CursoEntidadeJpa();
        entidade.id = registro.id();
        entidade.nome = registro.nome();
        entidade.faculdadeId = registro.faculdadeId();
        return entidade;
    }
}
