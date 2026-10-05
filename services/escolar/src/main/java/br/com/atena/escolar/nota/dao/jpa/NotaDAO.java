package br.com.atena.escolar.nota.dao.jpa;

import br.com.atena.escolar.comum.dao.helper.ErroPersistenciaHelper;
import br.com.atena.escolar.nota.dao.INotaDAO;
import br.com.atena.escolar.nota.dominio.Nota;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class NotaDAO implements INotaDAO {

    private final INotaRepositorioJpa repositorio;

    public NotaDAO(INotaRepositorioJpa repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Nota> listar() {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findAll(Sort.by("id")).stream().map(this::dominio).toList();
                });
    }

    @Override
    public Optional<Nota> buscar(Long identificador) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findById(identificador).map(this::dominio);
                });
    }

    @Override
    public Nota salvar(Nota registro) {
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
    public List<Nota> doAlunoNaDisciplina(Long disciplinaId, Long alunoId) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio
                            .findByDisciplinaIdAndAlunoIdOrderByBimestre(disciplinaId, alunoId)
                            .stream()
                            .map(this::dominio)
                            .toList();
                });
    }

    @Override
    public boolean existePeriodo(Long disciplinaId, Long alunoId, int bimestre) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.existsByDisciplinaIdAndAlunoIdAndBimestre(
                            disciplinaId, alunoId, bimestre);
                });
    }

    private Nota dominio(NotaEntidadeJpa entidade) {
        return new Nota(
                entidade.id,
                entidade.disciplinaId,
                entidade.alunoId,
                entidade.bimestre,
                entidade.valor,
                entidade.recuperacao,
                entidade.prazo,
                entidade.liberada);
    }

    private NotaEntidadeJpa entidade(Nota registro) {
        NotaEntidadeJpa entidade = new NotaEntidadeJpa();
        entidade.id = registro.id();
        entidade.disciplinaId = registro.disciplinaId();
        entidade.alunoId = registro.alunoId();
        entidade.bimestre = registro.bimestre();
        entidade.valor = registro.valor();
        entidade.recuperacao = registro.recuperacao();
        entidade.prazo = registro.prazo();
        entidade.liberada = registro.liberada();
        return entidade;
    }

    @Override
    @Transactional
    public void salvarAusente(Nota registro) {
        ErroPersistenciaHelper.executar(
                () -> {
                    repositorio.prepararPeriodo(
                            registro.disciplinaId(), registro.alunoId(), registro.bimestre());

                    return null;
                });
    }
}
