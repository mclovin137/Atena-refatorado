package br.com.atena.escolar.materia.dao.jpa;

import br.com.atena.escolar.comum.dao.helper.ErroPersistenciaHelper;
import br.com.atena.escolar.materia.dao.IMateriaDAO;
import br.com.atena.escolar.materia.dominio.Materia;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MateriaDAO implements IMateriaDAO {

    private final IMateriaRepositorioJpa repositorio;

    public MateriaDAO(IMateriaRepositorioJpa repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Materia> listar() {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findAll(Sort.by("id")).stream().map(this::dominio).toList();
                });
    }

    @Override
    public Optional<Materia> buscar(Long identificador) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findById(identificador).map(this::dominio);
                });
    }

    @Override
    public Materia salvar(Materia registro) {
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

    private Materia dominio(MateriaEntidadeJpa entidade) {
        return new Materia(entidade.id, entidade.nome);
    }

    private MateriaEntidadeJpa entidade(Materia registro) {
        MateriaEntidadeJpa entidade = new MateriaEntidadeJpa();
        entidade.id = registro.id();
        entidade.nome = registro.nome();
        return entidade;
    }
}
