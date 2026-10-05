package br.com.atena.escolar.notaavaliacao.dao.jpa;

import br.com.atena.escolar.comum.dao.helper.ErroPersistenciaHelper;
import br.com.atena.escolar.notaavaliacao.dao.INotaAvaliacaoDAO;
import br.com.atena.escolar.notaavaliacao.dominio.NotaAvaliacao;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class NotaAvaliacaoDAO implements INotaAvaliacaoDAO {

    private final INotaAvaliacaoRepositorioJpa repositorio;

    public NotaAvaliacaoDAO(INotaAvaliacaoRepositorioJpa repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<NotaAvaliacao> listar() {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findAll(Sort.by("id")).stream().map(this::dominio).toList();
                });
    }

    @Override
    public Optional<NotaAvaliacao> buscar(Long identificador) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findById(identificador).map(this::dominio);
                });
    }

    @Override
    public NotaAvaliacao salvar(NotaAvaliacao registro) {
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
    public List<NotaAvaliacao> daAvaliacao(Long avaliacaoId) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findByAvaliacaoId(avaliacaoId).stream()
                            .map(this::dominio)
                            .toList();
                });
    }

    private NotaAvaliacao dominio(NotaAvaliacaoEntidadeJpa entidade) {
        return new NotaAvaliacao(
                entidade.id, entidade.avaliacaoId, entidade.alunoId, entidade.valor);
    }

    private NotaAvaliacaoEntidadeJpa entidade(NotaAvaliacao registro) {
        NotaAvaliacaoEntidadeJpa entidade = new NotaAvaliacaoEntidadeJpa();
        entidade.id = registro.id();
        entidade.avaliacaoId = registro.avaliacaoId();
        entidade.alunoId = registro.alunoId();
        entidade.valor = registro.valor();
        return entidade;
    }
}
