package br.com.atena.escolar.serie.dao.jpa;

import br.com.atena.escolar.comum.dao.helper.ErroPersistenciaHelper;
import br.com.atena.escolar.serie.dao.ISerieDAO;
import br.com.atena.escolar.serie.dominio.Serie;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SerieDAO implements ISerieDAO {

    private final ISerieRepositorioJpa repositorio;

    public SerieDAO(ISerieRepositorioJpa repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Serie> listar() {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findAll(Sort.by("id")).stream().map(this::dominio).toList();
                });
    }

    @Override
    public Optional<Serie> buscar(Long identificador) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findById(identificador).map(this::dominio);
                });
    }

    @Override
    public Serie salvar(Serie registro) {
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

    private Serie dominio(SerieEntidadeJpa entidade) {
        return new Serie(entidade.id, entidade.nome);
    }

    private SerieEntidadeJpa entidade(Serie registro) {
        SerieEntidadeJpa entidade = new SerieEntidadeJpa();
        entidade.id = registro.id();
        entidade.nome = registro.nome();
        return entidade;
    }
}
