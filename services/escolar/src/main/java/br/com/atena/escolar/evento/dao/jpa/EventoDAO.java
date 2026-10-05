package br.com.atena.escolar.evento.dao.jpa;

import br.com.atena.escolar.comum.dao.helper.ErroPersistenciaHelper;
import br.com.atena.escolar.evento.dao.IEventoDAO;
import br.com.atena.escolar.evento.dominio.Evento;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EventoDAO implements IEventoDAO {

    private final IEventoRepositorioJpa repositorio;

    public EventoDAO(IEventoRepositorioJpa repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Evento> listar() {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findAll(Sort.by("id")).stream().map(this::dominio).toList();
                });
    }

    @Override
    public Optional<Evento> buscar(Long identificador) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findById(identificador).map(this::dominio);
                });
    }

    @Override
    public Evento salvar(Evento registro) {
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

    private Evento dominio(EventoEntidadeJpa entidade) {
        return new Evento(
                entidade.id, entidade.titulo, entidade.inicio, entidade.fim, entidade.descricao);
    }

    private EventoEntidadeJpa entidade(Evento registro) {
        EventoEntidadeJpa entidade = new EventoEntidadeJpa();
        entidade.id = registro.id();
        entidade.titulo = registro.titulo();
        entidade.inicio = registro.inicio();
        entidade.fim = registro.fim();
        entidade.descricao = registro.descricao();
        return entidade;
    }
}
