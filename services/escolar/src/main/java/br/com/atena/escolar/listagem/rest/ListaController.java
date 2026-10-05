package br.com.atena.escolar.listagem.rest;

import br.com.atena.escolar.listagem.actor.ConsultarListaActor;
import br.com.atena.escolar.listagem.dto.ListaRespostaDTO;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ListaController {
    private final ConsultarListaActor actor;

    public ListaController(ConsultarListaActor actor) {
        this.actor = actor;
    }

    @GetMapping("/api/internal/listas/{tipo}")
    public ListaRespostaDTO consultar(@PathVariable String tipo) {
        return actor.consultar(tipo);
    }
}
