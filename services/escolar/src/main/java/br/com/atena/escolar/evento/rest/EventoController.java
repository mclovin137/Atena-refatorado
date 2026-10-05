package br.com.atena.escolar.evento.rest;

import br.com.atena.escolar.evento.actor.ManterEventoActor;
import br.com.atena.escolar.evento.dto.EventoRequisicaoDTO;
import br.com.atena.escolar.evento.dto.EventoRespostaDTO;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    private final ManterEventoActor manter;

    public EventoController(ManterEventoActor manter) {
        this.manter = manter;
    }

    @GetMapping
    public List<EventoRespostaDTO> listar() {
        return manter.listar();
    }

    @GetMapping("/{id}")
    public EventoRespostaDTO consultar(@PathVariable Long id) {
        return manter.consultar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventoRespostaDTO criar(@Valid @RequestBody EventoRequisicaoDTO requisicao) {
        return manter.criar(requisicao);
    }

    @PutMapping("/{id}")
    public EventoRespostaDTO atualizar(
            @PathVariable Long id, @Valid @RequestBody EventoRequisicaoDTO requisicao) {
        return manter.atualizar(id, requisicao);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        manter.excluir(id);
    }
}
