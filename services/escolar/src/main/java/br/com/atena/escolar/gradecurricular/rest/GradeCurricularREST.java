package br.com.atena.escolar.gradecurricular.rest;

import br.com.atena.escolar.gradecurricular.actor.ManterGradeCurricularActor;
import br.com.atena.escolar.gradecurricular.dto.GradeCurricularRequisicaoDTO;
import br.com.atena.escolar.gradecurricular.dto.GradeCurricularRespostaDTO;

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
@RequestMapping("/api/grades-curriculares")
public class GradeCurricularREST {
    private final ManterGradeCurricularActor manter;

    public GradeCurricularREST(ManterGradeCurricularActor manter) {
        this.manter = manter;
    }

    @GetMapping
    public List<GradeCurricularRespostaDTO> listar() {
        return manter.listar();
    }

    @GetMapping("/{id}")
    public GradeCurricularRespostaDTO consultar(@PathVariable Long id) {
        return manter.consultar(id);
    }

    @CacheEvict(value = "catalogos", allEntries = true)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GradeCurricularRespostaDTO criar(
            @Valid @RequestBody GradeCurricularRequisicaoDTO requisicao) {
        return manter.criar(requisicao);
    }

    @CacheEvict(value = "catalogos", allEntries = true)
    @PutMapping("/{id}")
    public GradeCurricularRespostaDTO atualizar(
            @PathVariable Long id, @Valid @RequestBody GradeCurricularRequisicaoDTO requisicao) {
        return manter.atualizar(id, requisicao);
    }

    @CacheEvict(value = "catalogos", allEntries = true)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        manter.excluir(id);
    }
}
