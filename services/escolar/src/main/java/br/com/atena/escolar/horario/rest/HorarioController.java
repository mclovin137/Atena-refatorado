package br.com.atena.escolar.horario.rest;

import br.com.atena.escolar.horario.actor.ManterHorarioActor;
import br.com.atena.escolar.horario.dto.HorarioRequisicaoDTO;
import br.com.atena.escolar.horario.dto.HorarioRespostaDTO;

import jakarta.validation.Valid;

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
@RequestMapping("/api/horarios")
public class HorarioController {

    private final ManterHorarioActor manter;

    public HorarioController(ManterHorarioActor manter) {
        this.manter = manter;
    }

    @GetMapping
    public List<HorarioRespostaDTO> listar() {
        return manter.listar();
    }

    @GetMapping("/{id}")
    public HorarioRespostaDTO consultar(@PathVariable Long id) {
        return manter.consultar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HorarioRespostaDTO criar(@Valid @RequestBody HorarioRequisicaoDTO requisicao) {
        return manter.criar(requisicao);
    }

    @PutMapping("/{id}")
    public HorarioRespostaDTO atualizar(
            @PathVariable Long id, @Valid @RequestBody HorarioRequisicaoDTO requisicao) {
        return manter.atualizar(id, requisicao);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        manter.excluir(id);
    }
}
