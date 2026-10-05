package br.com.atena.relatorios.lote.dao;

import br.com.atena.relatorios.lote.dto.LinhaLoteDTO;
import br.com.atena.relatorios.snapshot.actor.PrepararSnapshotActor;
import br.com.atena.relatorios.snapshot.dto.LinhaSnapshotDTO;

import org.springframework.batch.item.ItemReader;

import java.util.Iterator;

/** Cada tentativa relê o snapshot salvo; o writer faz upsert por número. */
public class LeitorSnapshot implements ItemReader<LinhaLoteDTO> {
    private final PrepararSnapshotActor snapshots;
    private final Long reportId;
    private Iterator<LinhaSnapshotDTO> linhas;
    private int numero;

    public LeitorSnapshot(PrepararSnapshotActor snapshots, Long reportId) {
        this.snapshots = snapshots;
        this.reportId = reportId;
    }

    @Override
    public LinhaLoteDTO read() throws Exception {
        if (linhas == null) {
            linhas = snapshots.preparar(reportId).linhas().iterator();
        }
        if (!linhas.hasNext()) {
            return null;
        }
        return new LinhaLoteDTO(reportId, numero++, linhas.next());
    }
}
