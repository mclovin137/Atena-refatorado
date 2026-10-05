package br.com.atena.escolar.horario.dao.jpa;

import br.com.atena.escolar.comum.dao.helper.ErroPersistenciaHelper;
import br.com.atena.escolar.horario.dao.IHorarioDAO;
import br.com.atena.escolar.horario.dominio.Horario;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class HorarioDAO implements IHorarioDAO {

    private final IHorarioRepositorioJpa repositorio;

    public HorarioDAO(IHorarioRepositorioJpa repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Horario> listar() {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findAll(Sort.by("id")).stream().map(this::dominio).toList();
                });
    }

    @Override
    public Optional<Horario> buscar(Long identificador) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findById(identificador).map(this::dominio);
                });
    }

    @Override
    public Horario salvar(Horario registro) {
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

    private Horario dominio(HorarioEntidadeJpa entidade) {
        return new Horario(
                entidade.id,
                entidade.disciplinaId,
                entidade.diaSemana,
                entidade.inicio,
                entidade.fim);
    }

    private HorarioEntidadeJpa entidade(Horario registro) {
        HorarioEntidadeJpa entidade = new HorarioEntidadeJpa();
        entidade.id = registro.id();
        entidade.disciplinaId = registro.disciplinaId();
        entidade.diaSemana = registro.diaSemana();
        entidade.inicio = registro.inicio();
        entidade.fim = registro.fim();
        return entidade;
    }
}
