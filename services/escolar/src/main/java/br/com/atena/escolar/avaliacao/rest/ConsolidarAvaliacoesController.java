package br.com.atena.escolar.avaliacao.rest;

import br.com.atena.escolar.avaliacao.actor.ConsolidarAvaliacoesActor;
import br.com.atena.escolar.avaliacao.dto.ConsolidarAvaliacoesRequisicaoDTO;
import br.com.atena.escolar.nota.dto.NotaRespostaDTO;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ConsolidarAvaliacoesController {
    private final ConsolidarAvaliacoesActor consolidar;

    public ConsolidarAvaliacoesController(ConsolidarAvaliacoesActor consolidar) {
        this.consolidar = consolidar;
    }

    @PostMapping("/api/disciplinas/{id}/consolidar-avaliacoes")
    public List<NotaRespostaDTO> executar(
            @PathVariable Long id,
            @Valid @RequestBody ConsolidarAvaliacoesRequisicaoDTO requisicao) {
        return consolidar.executar(id, requisicao);
    }
}
