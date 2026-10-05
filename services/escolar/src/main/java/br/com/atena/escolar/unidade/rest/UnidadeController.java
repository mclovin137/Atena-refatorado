package br.com.atena.escolar.unidade.rest;

import br.com.atena.escolar.unidade.actor.ManterUnidadeActor;
import br.com.atena.escolar.unidade.dto.UnidadeRequisicaoDTO;
import br.com.atena.escolar.unidade.dto.UnidadeRespostaDTO;

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
@RequestMapping("/api/unidades")
public class UnidadeController {

    private final ManterUnidadeActor manter;

    public UnidadeController(ManterUnidadeActor manter) {
        this.manter = manter;
    }

    @GetMapping
    public List<UnidadeRespostaDTO> listar() {
        return manter.listar();
    }

    @GetMapping("/{id}")
    public UnidadeRespostaDTO consultar(@PathVariable Long id) {
        return manter.consultar(id);
    }

    @CacheEvict(value = "catalogos", allEntries = true)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UnidadeRespostaDTO criar(@Valid @RequestBody UnidadeRequisicaoDTO requisicao) {
        return manter.criar(requisicao);
    }

    @CacheEvict(value = "catalogos", allEntries = true)
    @PutMapping("/{id}")
    public UnidadeRespostaDTO atualizar(
            @PathVariable Long id, @Valid @RequestBody UnidadeRequisicaoDTO requisicao) {
        return manter.atualizar(id, requisicao);
    }

    @CacheEvict(value = "catalogos", allEntries = true)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        manter.excluir(id);
    }
}
