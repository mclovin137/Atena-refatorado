package br.com.atena.escolar.unidade.dao.jpa;

import br.com.atena.escolar.comum.dao.helper.ErroPersistenciaHelper;
import br.com.atena.escolar.unidade.dao.IUnidadeDAO;
import br.com.atena.escolar.unidade.dominio.Unidade;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UnidadeDAO implements IUnidadeDAO {

    private final IUnidadeRepositorioJpa repositorio;

    public UnidadeDAO(IUnidadeRepositorioJpa repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Unidade> listar() {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findAll(Sort.by("id")).stream().map(this::dominio).toList();
                });
    }

    @Override
    public Optional<Unidade> buscar(Long identificador) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findById(identificador).map(this::dominio);
                });
    }

    @Override
    public Unidade salvar(Unidade registro) {
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

    private Unidade dominio(UnidadeEntidadeJpa entidade) {
        return new Unidade(
                entidade.id, entidade.nome, entidade.endereco, entidade.telefone, entidade.cnpj);
    }

    private UnidadeEntidadeJpa entidade(Unidade registro) {
        UnidadeEntidadeJpa entidade = new UnidadeEntidadeJpa();
        entidade.id = registro.id();
        entidade.nome = registro.nome();
        entidade.endereco = registro.endereco();
        entidade.telefone = registro.telefone();
        entidade.cnpj = registro.cnpj();
        return entidade;
    }
}
