package br.com.atena.relatorios.relatorio.dao.jpa;

import br.com.atena.relatorios.relatorio.dao.IRelatorioDAO;
import br.com.atena.relatorios.relatorio.dominio.ErroRelatorio;
import br.com.atena.relatorios.relatorio.dominio.Relatorio;
import br.com.atena.relatorios.relatorio.dominio.SolicitacaoRelatorio;
import br.com.atena.relatorios.relatorio.dominio.TipoRelatorioEnum;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RelatorioDAO implements IRelatorioDAO {
    private final IRelatorioJpaRepository repositorio;

    public RelatorioDAO(IRelatorioJpaRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public Optional<Relatorio> buscar(Long id) {
        return repositorio.findById(id).map(this::dominio);
    }

    @Override
    public Optional<Relatorio> buscarSolicitacao(Long ownerId, String requestId) {
        return repositorio.findByOwnerIdAndRequestId(ownerId, requestId).map(this::dominio);
    }

    @Override
    public Relatorio criar(SolicitacaoRelatorio solicitacao) {
        RelatorioEntidade entidade = new RelatorioEntidade();
        entidade.alunoId = solicitacao.alunoId();
        entidade.ownerId = solicitacao.ownerId();
        entidade.professorId = solicitacao.professorId();
        entidade.releasedOnly = solicitacao.releasedOnly();
        entidade.tipo = solicitacao.tipo().name();
        entidade.requestId = solicitacao.requestId();
        try {
            return dominio(repositorio.saveAndFlush(entidade));
        } catch (DataIntegrityViolationException erro) {
            throw new ErroRelatorio(
                    ErroRelatorio.CategoriaEnum.CONFLITO,
                    "Registro duplicado ou vínculo impede a operação",
                    erro);
        }
    }

    @Override
    public void registrarExecucao(Long id, Long executionId) {
        repositorio.registrarExecucao(id, executionId);
    }

    @Override
    public void registrarSnapshot(Long id, String nome, String snapshot) {
        repositorio.registrarSnapshot(id, nome, snapshot);
    }

    private Relatorio dominio(RelatorioEntidade entidade) {
        SolicitacaoRelatorio solicitacao =
                new SolicitacaoRelatorio(
                        entidade.alunoId,
                        entidade.ownerId,
                        entidade.professorId,
                        entidade.releasedOnly,
                        TipoRelatorioEnum.valueOf(entidade.tipo),
                        entidade.requestId);
        return new Relatorio(
                entidade.id, solicitacao, entidade.executionId, entidade.nome, entidade.snapshot);
    }
}
