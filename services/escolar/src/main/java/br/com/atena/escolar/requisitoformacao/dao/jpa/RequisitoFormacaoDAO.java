package br.com.atena.escolar.requisitoformacao.dao.jpa;

import br.com.atena.escolar.comum.dao.helper.ErroPersistenciaHelper;
import br.com.atena.escolar.requisitoformacao.dao.IRequisitoFormacaoDAO;
import br.com.atena.escolar.requisitoformacao.dominio.RequisitoFormacao;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RequisitoFormacaoDAO implements IRequisitoFormacaoDAO {
    private final IRequisitoFormacaoRepositorioJpa repositorio;

    public RequisitoFormacaoDAO(IRequisitoFormacaoRepositorioJpa repositorio) {
        this.repositorio = repositorio;
    }

    public List<RequisitoFormacao> listar() {
        return ErroPersistenciaHelper.executar(
                () -> repositorio.findAll(Sort.by("id")).stream().map(this::dominio).toList());
    }

    public Optional<RequisitoFormacao> buscar(Long identificador) {
        return ErroPersistenciaHelper.executar(
                () -> repositorio.findById(identificador).map(this::dominio));
    }

    public RequisitoFormacao salvar(RequisitoFormacao registro) {
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

    private RequisitoFormacao dominio(RequisitoFormacaoEntidadeJpa entidade) {
        return new RequisitoFormacao(
                entidade.id, entidade.nome, entidade.gradeId, entidade.descricao);
    }

    private RequisitoFormacaoEntidadeJpa entidade(RequisitoFormacao registro) {
        var entidade = new RequisitoFormacaoEntidadeJpa();
        entidade.id = registro.id();
        entidade.nome = registro.nome();
        entidade.gradeId = registro.gradeId();
        entidade.descricao = registro.descricao();
        return entidade;
    }
}
