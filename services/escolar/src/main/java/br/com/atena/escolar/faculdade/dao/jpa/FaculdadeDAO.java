package br.com.atena.escolar.faculdade.dao.jpa;

import br.com.atena.escolar.comum.dao.helper.ErroPersistenciaHelper;
import br.com.atena.escolar.faculdade.dao.IFaculdadeDAO;
import br.com.atena.escolar.faculdade.dominio.Faculdade;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FaculdadeDAO implements IFaculdadeDAO {
    private final IFaculdadeRepositorioJpa repositorio;

    public FaculdadeDAO(IFaculdadeRepositorioJpa repositorio) {
        this.repositorio = repositorio;
    }

    public List<Faculdade> listar() {
        return ErroPersistenciaHelper.executar(
                () -> repositorio.findAll(Sort.by("id")).stream().map(this::dominio).toList());
    }

    public Optional<Faculdade> buscar(Long identificador) {
        return ErroPersistenciaHelper.executar(
                () -> repositorio.findById(identificador).map(this::dominio));
    }

    public Faculdade salvar(Faculdade registro) {
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

    private Faculdade dominio(FaculdadeEntidadeJpa entidade) {
        return new Faculdade(entidade.id, entidade.nome);
    }

    private FaculdadeEntidadeJpa entidade(Faculdade registro) {
        var entidade = new FaculdadeEntidadeJpa();
        entidade.id = registro.id();
        entidade.nome = registro.nome();
        return entidade;
    }
}
