package br.com.atena.escolar.matricula.dao.jpa;

import br.com.atena.escolar.comum.dao.helper.ErroPersistenciaHelper;
import br.com.atena.escolar.matricula.dao.IMatriculaDAO;
import br.com.atena.escolar.matricula.dominio.Matricula;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MatriculaDAO implements IMatriculaDAO {

    private final IMatriculaRepositorioJpa repositorio;

    public MatriculaDAO(IMatriculaRepositorioJpa repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Matricula> listar() {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findAll(Sort.by("id")).stream().map(this::dominio).toList();
                });
    }

    @Override
    public Optional<Matricula> buscar(Long identificador) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findById(identificador).map(this::dominio);
                });
    }

    @Override
    public Matricula salvar(Matricula registro) {
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
    public List<Matricula> daTurma(Long turmaId) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findByTurmaId(turmaId).stream().map(this::dominio).toList();
                });
    }

    @Override
    public List<Matricula> doAluno(Long alunoId) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findByAlunoId(alunoId).stream().map(this::dominio).toList();
                });
    }

    @Override
    public boolean alunoNaTurma(Long turmaId, Long alunoId) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.existsByTurmaIdAndAlunoId(turmaId, alunoId);
                });
    }

    @Override
    public List<Matricula> paginaDaTurma(Long turmaId, int pagina, int tamanho) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio
                            .findByTurmaId(turmaId, PageRequest.of(pagina, tamanho, Sort.by("id")))
                            .getContent()
                            .stream()
                            .map(this::dominio)
                            .toList();
                });
    }

    private Matricula dominio(MatriculaEntidadeJpa entidade) {
        return new Matricula(entidade.id, entidade.turmaId, entidade.alunoId, entidade.dependencia);
    }

    private MatriculaEntidadeJpa entidade(Matricula registro) {
        MatriculaEntidadeJpa entidade = new MatriculaEntidadeJpa();
        entidade.id = registro.id();
        entidade.turmaId = registro.turmaId();
        entidade.alunoId = registro.alunoId();
        entidade.dependencia = registro.dependencia();
        return entidade;
    }
}
