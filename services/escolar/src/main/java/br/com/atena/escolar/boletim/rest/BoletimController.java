package br.com.atena.escolar.boletim.rest;

import br.com.atena.escolar.boletim.actor.ConsultarBoletimActor;
import br.com.atena.escolar.boletim.dto.BoletimRespostaDTO;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BoletimController {
    private final ConsultarBoletimActor consultar;

    public BoletimController(ConsultarBoletimActor consultar) {
        this.consultar = consultar;
    }

    @GetMapping("/api/boletins/{alunoId}")
    public BoletimRespostaDTO consultar(@PathVariable Long alunoId) {
        return consultar.consultar(alunoId);
    }

    @GetMapping("/api/internal/snapshot/{alunoId}")
    public BoletimRespostaDTO snapshot(
            @PathVariable Long alunoId,
            @RequestParam(required = false) Long professorId,
            @RequestParam(defaultValue = "false") boolean releasedOnly) {
        return consultar.snapshot(alunoId, professorId, releasedOnly);
    }
}
