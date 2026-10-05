package br.com.atena.escolar.curriculo.rest;

import br.com.atena.escolar.curriculo.actor.AplicarCurriculoActor;
import br.com.atena.escolar.curriculo.dto.AplicarCurriculoRequisicaoDTO;
import br.com.atena.escolar.disciplina.dto.DisciplinaRespostaDTO;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AplicarCurriculoController {
    private final AplicarCurriculoActor aplicar;

    public AplicarCurriculoController(AplicarCurriculoActor aplicar) {
        this.aplicar = aplicar;
    }

    @PostMapping("/api/turmas/{id}/aplicar-curriculo")
    public List<DisciplinaRespostaDTO> executar(
            @PathVariable Long id, @Valid @RequestBody AplicarCurriculoRequisicaoDTO requisicao) {
        return aplicar.executar(id, requisicao);
    }
}
