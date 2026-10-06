package br.com.atena.escolar.matriculamateria.rest;

import br.com.atena.escolar.matriculamateria.actor.ManterMatriculaMateriaActor;
import br.com.atena.escolar.matriculamateria.dto.MatriculaMateriaRequisicaoDTO;
import br.com.atena.escolar.matriculamateria.dto.MatriculaMateriaRespostaDTO;

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
@RequestMapping("/api/matriculas/{id}/materias")
public class MatriculaMateriaREST {
    private final ManterMatriculaMateriaActor manter;

    public MatriculaMateriaREST(ManterMatriculaMateriaActor manter) {
        this.manter = manter;
    }

    @GetMapping
    public List<MatriculaMateriaRespostaDTO> listar(@PathVariable Long id) {
        return manter.listar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MatriculaMateriaRespostaDTO associar(
            @PathVariable Long id, @Valid @RequestBody MatriculaMateriaRequisicaoDTO requisicao) {
        return manter.associar(id, requisicao.disciplinaId());
    }

    @DeleteMapping("/{associadoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id, @PathVariable Long associadoId) {
        manter.excluir(id, associadoId);
    }
}
