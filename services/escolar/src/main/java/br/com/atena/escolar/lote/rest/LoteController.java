package br.com.atena.escolar.lote.rest;

import br.com.atena.escolar.lote.actor.InicializarNotasActor;
import br.com.atena.escolar.lote.dto.ExecucaoLoteRespostaDTO;
import br.com.atena.escolar.lote.dto.InicializarNotasRequisicaoDTO;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jobs")
public class LoteController {
    private final InicializarNotasActor inicializar;

    public LoteController(InicializarNotasActor inicializar) {
        this.inicializar = inicializar;
    }

    @PostMapping("/inicializar-notas")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ExecucaoLoteRespostaDTO iniciar(
            @Valid @RequestBody InicializarNotasRequisicaoDTO requisicao) {
        return inicializar.iniciar(requisicao);
    }

    @GetMapping("/{id}")
    public ExecucaoLoteRespostaDTO consultar(@PathVariable Long id) {
        return inicializar.consultar(id);
    }
}
