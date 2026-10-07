package br.com.atena.escolar.requisitoformacao.rest;

import br.com.atena.escolar.requisitoformacao.actor.ManterRequisitoFormacaoActor;
import br.com.atena.escolar.requisitoformacao.dto.RequisitoFormacaoRequisicaoDTO;
import br.com.atena.escolar.requisitoformacao.dto.RequisitoFormacaoRespostaDTO;

import jakarta.validation.Valid;

import org.springframework.cache.annotation.CacheEvict;
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
@RequestMapping("/api/requisitos-formacao")
public class RequisitoFormacaoREST {
    private final ManterRequisitoFormacaoActor manter;

    public RequisitoFormacaoREST(ManterRequisitoFormacaoActor manter) {
        this.manter = manter;
    }

    @GetMapping
    public List<RequisitoFormacaoRespostaDTO> listar() {
        return manter.listar();
    }

    @GetMapping("/{id}")
    public RequisitoFormacaoRespostaDTO consultar(@PathVariable Long id) {
        return manter.consultar(id);
    }

    @CacheEvict(value = "catalogos", allEntries = true)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequisitoFormacaoRespostaDTO criar(
            @Valid @RequestBody RequisitoFormacaoRequisicaoDTO requisicao) {
        return manter.criar(requisicao);
    }

    @CacheEvict(value = "catalogos", allEntries = true)
    @PutMapping("/{id}")
    public RequisitoFormacaoRespostaDTO atualizar(
            @PathVariable Long id, @Valid @RequestBody RequisitoFormacaoRequisicaoDTO requisicao) {
        return manter.atualizar(id, requisicao);
    }

    @CacheEvict(value = "catalogos", allEntries = true)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        manter.excluir(id);
    }
}
