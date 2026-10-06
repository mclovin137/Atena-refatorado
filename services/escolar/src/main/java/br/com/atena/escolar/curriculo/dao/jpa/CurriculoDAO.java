package br.com.atena.escolar.curriculo.dao.jpa;

import br.com.atena.escolar.comum.dao.helper.ErroPersistenciaHelper;
import br.com.atena.escolar.curriculo.dao.ICurriculoDAO;
import br.com.atena.escolar.curriculo.dominio.Curriculo;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CurriculoDAO implements ICurriculoDAO {

    private final ICurriculoRepositorioJpa repositorio;

    public CurriculoDAO(ICurriculoRepositorioJpa repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Curriculo> listar() {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findAll(Sort.by("id")).stream().map(this::dominio).toList();
                });
    }

    @Override
    public Optional<Curriculo> buscar(Long identificador) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findById(identificador).map(this::dominio);
                });
    }

    @Override
    public Curriculo salvar(Curriculo registro) {
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

    private Curriculo dominio(CurriculoEntidadeJpa entidade) {
        return new Curriculo(
                entidade.id,
                entidade.nome,
                entidade.materiaId,
                entidade.cargaHoraria,
                entidade.gradeId);
    }

    private CurriculoEntidadeJpa entidade(Curriculo registro) {
        CurriculoEntidadeJpa entidade = new CurriculoEntidadeJpa();
        entidade.id = registro.id();
        entidade.nome = registro.nome();
        entidade.materiaId = registro.materiaId();
        entidade.cargaHoraria = registro.cargaHoraria();
        entidade.gradeId = registro.gradeId();
        return entidade;
    }
}
