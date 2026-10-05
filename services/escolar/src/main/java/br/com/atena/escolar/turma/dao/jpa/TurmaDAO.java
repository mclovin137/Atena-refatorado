package br.com.atena.escolar.turma.dao.jpa;

import br.com.atena.escolar.comum.dao.helper.ErroPersistenciaHelper;
import br.com.atena.escolar.turma.dao.ITurmaDAO;
import br.com.atena.escolar.turma.dominio.Turma;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TurmaDAO implements ITurmaDAO {

    private final ITurmaRepositorioJpa repositorio;

    public TurmaDAO(ITurmaRepositorioJpa repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Turma> listar() {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findAll(Sort.by("id")).stream().map(this::dominio).toList();
                });
    }

    @Override
    public Optional<Turma> buscar(Long identificador) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findById(identificador).map(this::dominio);
                });
    }

    @Override
    public Turma salvar(Turma registro) {
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

    private Turma dominio(TurmaEntidadeJpa entidade) {
        return new Turma(
                entidade.id, entidade.cursoId, entidade.serieId, entidade.ano, entidade.nome);
    }

    private TurmaEntidadeJpa entidade(Turma registro) {
        TurmaEntidadeJpa entidade = new TurmaEntidadeJpa();
        entidade.id = registro.id();
        entidade.cursoId = registro.cursoId();
        entidade.serieId = registro.serieId();
        entidade.ano = registro.ano();
        entidade.nome = registro.nome();
        return entidade;
    }
}
