package br.com.atena.escolar.presenca.rest;

import br.com.atena.escolar.presenca.actor.ManterPresencaActor;
import br.com.atena.escolar.presenca.dto.PresencaRequisicaoDTO;
import br.com.atena.escolar.presenca.dto.PresencaRespostaDTO;

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
@RequestMapping("/api/presencas")
public class PresencaController {

    private final ManterPresencaActor manter;

    public PresencaController(ManterPresencaActor manter) {
        this.manter = manter;
    }

    @GetMapping
    public List<PresencaRespostaDTO> listar() {
        return manter.listar();
    }

    @GetMapping("/{id}")
    public PresencaRespostaDTO consultar(@PathVariable Long id) {
        return manter.consultar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PresencaRespostaDTO criar(@Valid @RequestBody PresencaRequisicaoDTO requisicao) {
        return manter.criar(requisicao);
    }

    @PutMapping("/{id}")
    public PresencaRespostaDTO atualizar(
            @PathVariable Long id, @Valid @RequestBody PresencaRequisicaoDTO requisicao) {
        return manter.atualizar(id, requisicao);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        manter.excluir(id);
    }
}
