package br.com.atena.escolar.faculdade.rest;

import br.com.atena.escolar.faculdade.actor.ManterFaculdadeActor;
import br.com.atena.escolar.faculdade.dto.FaculdadeRequisicaoDTO;
import br.com.atena.escolar.faculdade.dto.FaculdadeRespostaDTO;

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
@RequestMapping("/api/faculdades")
public class FaculdadeREST {
    private final ManterFaculdadeActor manter;

    public FaculdadeREST(ManterFaculdadeActor manter) {
        this.manter = manter;
    }

    @GetMapping
    public List<FaculdadeRespostaDTO> listar() {
        return manter.listar();
    }

    @GetMapping("/{id}")
    public FaculdadeRespostaDTO consultar(@PathVariable Long id) {
        return manter.consultar(id);
    }

    @CacheEvict(value = "catalogos", allEntries = true)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FaculdadeRespostaDTO criar(@Valid @RequestBody FaculdadeRequisicaoDTO requisicao) {
        return manter.criar(requisicao);
    }

    @CacheEvict(value = "catalogos", allEntries = true)
    @PutMapping("/{id}")
    public FaculdadeRespostaDTO atualizar(
            @PathVariable Long id, @Valid @RequestBody FaculdadeRequisicaoDTO requisicao) {
        return manter.atualizar(id, requisicao);
    }

    @CacheEvict(value = "catalogos", allEntries = true)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        manter.excluir(id);
    }
}
