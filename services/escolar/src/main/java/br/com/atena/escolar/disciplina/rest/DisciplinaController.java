package br.com.atena.escolar.disciplina.rest;

import br.com.atena.escolar.disciplina.actor.ManterDisciplinaActor;
import br.com.atena.escolar.disciplina.dto.DisciplinaRequisicaoDTO;
import br.com.atena.escolar.disciplina.dto.DisciplinaRespostaDTO;

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
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private final ManterDisciplinaActor manter;

    public DisciplinaController(ManterDisciplinaActor manter) {
        this.manter = manter;
    }

    @GetMapping
    public List<DisciplinaRespostaDTO> listar() {
        return manter.listar();
    }

    @GetMapping("/{id}")
    public DisciplinaRespostaDTO consultar(@PathVariable Long id) {
        return manter.consultar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DisciplinaRespostaDTO criar(@Valid @RequestBody DisciplinaRequisicaoDTO requisicao) {
        return manter.criar(requisicao);
    }

    @PutMapping("/{id}")
    public DisciplinaRespostaDTO atualizar(
            @PathVariable Long id, @Valid @RequestBody DisciplinaRequisicaoDTO requisicao) {
        return manter.atualizar(id, requisicao);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        manter.excluir(id);
    }
}
