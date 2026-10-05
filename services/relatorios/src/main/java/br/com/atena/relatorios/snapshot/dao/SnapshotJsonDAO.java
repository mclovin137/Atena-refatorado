package br.com.atena.relatorios.snapshot.dao;

import br.com.atena.relatorios.snapshot.dto.LinhaSnapshotDTO;
import br.com.atena.relatorios.snapshot.dto.SnapshotDTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Repository;

@Repository
public class SnapshotJsonDAO {
    private final ObjectMapper mapper;

    public SnapshotJsonDAO(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public SnapshotDTO lerSnapshot(String conteudo) throws JsonProcessingException {
        return mapper.readValue(conteudo, SnapshotDTO.class);
    }

    public LinhaSnapshotDTO lerLinha(String conteudo) throws JsonProcessingException {
        return mapper.readValue(conteudo, LinhaSnapshotDTO.class);
    }

    public String gravarSnapshot(SnapshotDTO snapshot) throws JsonProcessingException {
        return mapper.writeValueAsString(snapshot);
    }

    public String gravarLinha(LinhaSnapshotDTO linha) throws JsonProcessingException {
        return mapper.writeValueAsString(linha);
    }
}
