package br.com.atena.escolar.materia.rest;

import br.com.atena.escolar.materia.actor.ManterMateriaActor;
import br.com.atena.escolar.materia.dto.MateriaRequisicaoDTO;
import br.com.atena.escolar.materia.dto.MateriaRespostaDTO;

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
@RequestMapping("/api/materias")
public class MateriaController {

    private final ManterMateriaActor manter;

    public MateriaController(ManterMateriaActor manter) {
        this.manter = manter;
    }

    @GetMapping
    public List<MateriaRespostaDTO> listar() {
        return manter.listar();
    }

    @GetMapping("/{id}")
    public MateriaRespostaDTO consultar(@PathVariable Long id) {
        return manter.consultar(id);
    }

    @CacheEvict(value = "catalogos", allEntries = true)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MateriaRespostaDTO criar(@Valid @RequestBody MateriaRequisicaoDTO requisicao) {
        return manter.criar(requisicao);
    }

    @CacheEvict(value = "catalogos", allEntries = true)
    @PutMapping("/{id}")
    public MateriaRespostaDTO atualizar(
            @PathVariable Long id, @Valid @RequestBody MateriaRequisicaoDTO requisicao) {
        return manter.atualizar(id, requisicao);
    }

    @CacheEvict(value = "catalogos", allEntries = true)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        manter.excluir(id);
    }
}
