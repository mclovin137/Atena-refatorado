package br.com.atena.escolar.gradecurricular.dao.jpa;

import br.com.atena.escolar.comum.dao.helper.ErroPersistenciaHelper;
import br.com.atena.escolar.gradecurricular.dao.IGradeCurricularDAO;
import br.com.atena.escolar.gradecurricular.dominio.GradeCurricular;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class GradeCurricularDAO implements IGradeCurricularDAO {
    private final IGradeCurricularRepositorioJpa repositorio;

    public GradeCurricularDAO(IGradeCurricularRepositorioJpa repositorio) {
        this.repositorio = repositorio;
    }

    public List<GradeCurricular> listar() {
        return ErroPersistenciaHelper.executar(
                () -> repositorio.findAll(Sort.by("id")).stream().map(this::dominio).toList());
    }

    public Optional<GradeCurricular> buscar(Long identificador) {
        return ErroPersistenciaHelper.executar(
                () -> repositorio.findById(identificador).map(this::dominio));
    }

    public GradeCurricular salvar(GradeCurricular registro) {
        return ErroPersistenciaHelper.executar(
                () -> dominio(repositorio.saveAndFlush(entidade(registro))));
    }

    public void excluir(Long identificador) {
        ErroPersistenciaHelper.executar(
                () -> {
                    repositorio.deleteById(identificador);
                    repositorio.flush();
                    return null;
                });
    }

    private GradeCurricular dominio(GradeCurricularEntidadeJpa entidade) {
        return new GradeCurricular(entidade.id, entidade.nome, entidade.cursoId);
    }

    private GradeCurricularEntidadeJpa entidade(GradeCurricular registro) {
        var entidade = new GradeCurricularEntidadeJpa();
        entidade.id = registro.id();
        entidade.nome = registro.nome();
        entidade.cursoId = registro.cursoId();
        return entidade;
    }
}
