package br.com.atena.escolar.requisitomateria.rest;

import br.com.atena.escolar.requisitomateria.actor.ManterRequisitoMateriaActor;
import br.com.atena.escolar.requisitomateria.dto.RequisitoMateriaRequisicaoDTO;
import br.com.atena.escolar.requisitomateria.dto.RequisitoMateriaRespostaDTO;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/requisitos-formacao/{id}/materias")
public class RequisitoMateriaREST {
    private final ManterRequisitoMateriaActor manter;

    public RequisitoMateriaREST(ManterRequisitoMateriaActor manter) {
        this.manter = manter;
    }

    @GetMapping
    public List<RequisitoMateriaRespostaDTO> listar(@PathVariable Long id) {
        return manter.listar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequisitoMateriaRespostaDTO associar(
            @PathVariable Long id, @Valid @RequestBody RequisitoMateriaRequisicaoDTO requisicao) {
        return manter.associar(id, requisicao.materiaId());
    }

    @DeleteMapping("/{associadoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id, @PathVariable Long associadoId) {
        manter.excluir(id, associadoId);
    }
}
