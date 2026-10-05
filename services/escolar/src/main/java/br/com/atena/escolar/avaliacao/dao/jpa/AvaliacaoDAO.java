package br.com.atena.escolar.avaliacao.dao.jpa;

import br.com.atena.escolar.avaliacao.dao.IAvaliacaoDAO;
import br.com.atena.escolar.avaliacao.dominio.Avaliacao;
import br.com.atena.escolar.comum.dao.helper.ErroPersistenciaHelper;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AvaliacaoDAO implements IAvaliacaoDAO {

    private final IAvaliacaoRepositorioJpa repositorio;

    public AvaliacaoDAO(IAvaliacaoRepositorioJpa repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Avaliacao> listar() {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findAll(Sort.by("id")).stream().map(this::dominio).toList();
                });
    }

    @Override
    public Optional<Avaliacao> buscar(Long identificador) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findById(identificador).map(this::dominio);
                });
    }

    @Override
    public Avaliacao salvar(Avaliacao registro) {
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

    private Avaliacao dominio(AvaliacaoEntidadeJpa entidade) {
        return new Avaliacao(
                entidade.id,
                entidade.disciplinaId,
                entidade.nome,
                entidade.bimestre,
                entidade.valor,
                entidade.data,
                entidade.tipo);
    }

    private AvaliacaoEntidadeJpa entidade(Avaliacao registro) {
        AvaliacaoEntidadeJpa entidade = new AvaliacaoEntidadeJpa();
        entidade.id = registro.id();
        entidade.disciplinaId = registro.disciplinaId();
        entidade.nome = registro.nome();
        entidade.bimestre = registro.bimestre();
        entidade.valor = registro.valor();
        entidade.data = registro.data();
        entidade.tipo = registro.tipo();
        return entidade;
    }
}
