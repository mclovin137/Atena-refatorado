package br.com.atena.relatorios.linha.dao.jpa;

import br.com.atena.relatorios.linha.dao.ILinhaDAO;
import br.com.atena.relatorios.snapshot.dao.SnapshotJsonDAO;
import br.com.atena.relatorios.snapshot.dto.LinhaSnapshotDTO;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class LinhaDAO implements ILinhaDAO {
    private final ILinhaJpaRepository repositorio;
    private final SnapshotJsonDAO json;

    public LinhaDAO(ILinhaJpaRepository repositorio, SnapshotJsonDAO json) {
        this.repositorio = repositorio;
        this.json = json;
    }

    @Override
    public List<LinhaSnapshotDTO> listar(Long relatorioId) throws java.io.IOException {
        List<LinhaSnapshotDTO> linhas = new ArrayList<>();
        for (LinhaEntidade entidade : repositorio.findByRelatorioIdOrderByNumero(relatorioId)) {
            linhas.add(json.lerLinha(entidade.conteudo));
        }
        return linhas;
    }

    @Override
    public void gravar(Long relatorioId, int numero, LinhaSnapshotDTO conteudo)
            throws java.io.IOException {
        LinhaEntidade entidade =
                repositorio
                        .findByRelatorioIdAndNumero(relatorioId, numero)
                        .orElseGet(LinhaEntidade::new);
        entidade.relatorioId = relatorioId;
        entidade.numero = numero;
        entidade.conteudo = json.gravarLinha(conteudo);
        repositorio.save(entidade);
    }
}
