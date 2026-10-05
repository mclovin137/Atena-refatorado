package br.com.atena.escolar.curso.rest;

import br.com.atena.escolar.curso.actor.ManterCursoActor;
import br.com.atena.escolar.curso.dto.CursoRequisicaoDTO;
import br.com.atena.escolar.curso.dto.CursoRespostaDTO;

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
@RequestMapping("/api/cursos")
public class CursoController {

    private final ManterCursoActor manter;

    public CursoController(ManterCursoActor manter) {
        this.manter = manter;
    }

    @GetMapping
    public List<CursoRespostaDTO> listar() {
        return manter.listar();
    }

    @GetMapping("/{id}")
    public CursoRespostaDTO consultar(@PathVariable Long id) {
        return manter.consultar(id);
    }

    @CacheEvict(value = "catalogos", allEntries = true)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CursoRespostaDTO criar(@Valid @RequestBody CursoRequisicaoDTO requisicao) {
        return manter.criar(requisicao);
    }

    @CacheEvict(value = "catalogos", allEntries = true)
    @PutMapping("/{id}")
    public CursoRespostaDTO atualizar(
            @PathVariable Long id, @Valid @RequestBody CursoRequisicaoDTO requisicao) {
        return manter.atualizar(id, requisicao);
    }

    @CacheEvict(value = "catalogos", allEntries = true)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        manter.excluir(id);
    }
}
