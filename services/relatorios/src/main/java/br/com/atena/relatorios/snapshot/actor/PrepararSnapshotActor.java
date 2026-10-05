package br.com.atena.relatorios.snapshot.actor;

import br.com.atena.relatorios.relatorio.actor.ConsultarRelatorioActor;
import br.com.atena.relatorios.relatorio.dao.IRelatorioDAO;
import br.com.atena.relatorios.relatorio.dominio.Relatorio;
import br.com.atena.relatorios.relatorio.dominio.TipoRelatorioEnum;
import br.com.atena.relatorios.snapshot.dao.IFonteEscolarDAO;
import br.com.atena.relatorios.snapshot.dao.SnapshotJsonDAO;
import br.com.atena.relatorios.snapshot.dto.LinhaSnapshotDTO;
import br.com.atena.relatorios.snapshot.dto.SnapshotDTO;
import br.com.atena.relatorios.snapshot.service.BoletimService;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PrepararSnapshotActor {
    private final ConsultarRelatorioActor consulta;
    private final IRelatorioDAO relatorios;
    private final IFonteEscolarDAO fonte;
    private final SnapshotJsonDAO json;
    private final BoletimService boletim = new BoletimService();

    public PrepararSnapshotActor(
            ConsultarRelatorioActor consulta,
            IRelatorioDAO relatorios,
            IFonteEscolarDAO fonte,
            SnapshotJsonDAO json) {
        this.consulta = consulta;
        this.relatorios = relatorios;
        this.fonte = fonte;
        this.json = json;
    }

    public SnapshotDTO preparar(Long reportId) throws java.io.IOException {
        Relatorio relatorio = consulta.buscar(reportId);
        if (relatorio.snapshot() != null) {
            return json.lerSnapshot(relatorio.snapshot());
        }
        SnapshotDTO snapshot = fonte.carregar(relatorio.solicitacao());
        if (relatorio.solicitacao().tipo() == TipoRelatorioEnum.BOLETIM) {
            snapshot = filtrarBoletim(snapshot);
        }
        relatorios.registrarSnapshot(reportId, snapshot.nome(), json.gravarSnapshot(snapshot));
        return snapshot;
    }

    private SnapshotDTO filtrarBoletim(SnapshotDTO snapshot) {
        int ultimoAno =
                boletim.ultimoAno(snapshot.linhas().stream().map(LinhaSnapshotDTO::ano).toList());
        List<LinhaSnapshotDTO> linhas =
                snapshot.linhas().stream().filter(linha -> linha.ano() == ultimoAno).toList();
        return new SnapshotDTO(
                snapshot.alunoId(),
                snapshot.nome(),
                snapshot.matricula(),
                snapshot.documento(),
                snapshot.unidade(),
                linhas);
    }
}
