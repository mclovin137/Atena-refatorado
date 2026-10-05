package br.com.atena.escolar.curriculo.rest;

import br.com.atena.escolar.curriculo.actor.ManterCurriculoActor;
import br.com.atena.escolar.curriculo.dto.CurriculoRequisicaoDTO;
import br.com.atena.escolar.curriculo.dto.CurriculoRespostaDTO;

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
@RequestMapping("/api/curriculos")
public class CurriculoController {

    private final ManterCurriculoActor manter;

    public CurriculoController(ManterCurriculoActor manter) {
        this.manter = manter;
    }

    @GetMapping
    public List<CurriculoRespostaDTO> listar() {
        return manter.listar();
    }

    @GetMapping("/{id}")
    public CurriculoRespostaDTO consultar(@PathVariable Long id) {
        return manter.consultar(id);
    }

    @CacheEvict(value = "catalogos", allEntries = true)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CurriculoRespostaDTO criar(@Valid @RequestBody CurriculoRequisicaoDTO requisicao) {
        return manter.criar(requisicao);
    }

    @CacheEvict(value = "catalogos", allEntries = true)
    @PutMapping("/{id}")
    public CurriculoRespostaDTO atualizar(
            @PathVariable Long id, @Valid @RequestBody CurriculoRequisicaoDTO requisicao) {
        return manter.atualizar(id, requisicao);
    }

    @CacheEvict(value = "catalogos", allEntries = true)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        manter.excluir(id);
    }
}
