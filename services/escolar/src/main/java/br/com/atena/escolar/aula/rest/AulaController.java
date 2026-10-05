package br.com.atena.escolar.aula.rest;

import br.com.atena.escolar.aula.actor.ManterAulaActor;
import br.com.atena.escolar.aula.dto.AulaRequisicaoDTO;
import br.com.atena.escolar.aula.dto.AulaRespostaDTO;

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
@RequestMapping("/api/aulas")
public class AulaController {

    private final ManterAulaActor manter;

    public AulaController(ManterAulaActor manter) {
        this.manter = manter;
    }

    @GetMapping
    public List<AulaRespostaDTO> listar() {
        return manter.listar();
    }

    @GetMapping("/{id}")
    public AulaRespostaDTO consultar(@PathVariable Long id) {
        return manter.consultar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AulaRespostaDTO criar(@Valid @RequestBody AulaRequisicaoDTO requisicao) {
        return manter.criar(requisicao);
    }

    @PutMapping("/{id}")
    public AulaRespostaDTO atualizar(
            @PathVariable Long id, @Valid @RequestBody AulaRequisicaoDTO requisicao) {
        return manter.atualizar(id, requisicao);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        manter.excluir(id);
    }
}
