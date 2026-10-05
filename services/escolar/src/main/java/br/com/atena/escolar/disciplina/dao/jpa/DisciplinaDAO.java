package br.com.atena.escolar.disciplina.dao.jpa;

import br.com.atena.escolar.comum.dao.helper.ErroPersistenciaHelper;
import br.com.atena.escolar.disciplina.dao.IDisciplinaDAO;
import br.com.atena.escolar.disciplina.dominio.Disciplina;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DisciplinaDAO implements IDisciplinaDAO {

    private final IDisciplinaRepositorioJpa repositorio;

    public DisciplinaDAO(IDisciplinaRepositorioJpa repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Disciplina> listar() {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findAll(Sort.by("id")).stream().map(this::dominio).toList();
                });
    }

    @Override
    public Optional<Disciplina> buscar(Long identificador) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findById(identificador).map(this::dominio);
                });
    }

    @Override
    public Disciplina salvar(Disciplina registro) {
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

    @Override
    public List<Disciplina> daTurma(Long turmaId) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findByTurmaId(turmaId).stream().map(this::dominio).toList();
                });
    }

    private Disciplina dominio(DisciplinaEntidadeJpa entidade) {
        return new Disciplina(
                entidade.id,
                entidade.turmaId,
                entidade.materiaId,
                entidade.professorId,
                entidade.cargaHoraria);
    }

    private DisciplinaEntidadeJpa entidade(Disciplina registro) {
        DisciplinaEntidadeJpa entidade = new DisciplinaEntidadeJpa();
        entidade.id = registro.id();
        entidade.turmaId = registro.turmaId();
        entidade.materiaId = registro.materiaId();
        entidade.professorId = registro.professorId();
        entidade.cargaHoraria = registro.cargaHoraria();
        return entidade;
    }
}
