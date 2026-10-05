package br.com.atena.escolar.matricula.rest;

import br.com.atena.escolar.matricula.actor.ManterMatriculaActor;
import br.com.atena.escolar.matricula.dto.MatriculaRequisicaoDTO;
import br.com.atena.escolar.matricula.dto.MatriculaRespostaDTO;

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
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final ManterMatriculaActor manter;

    public MatriculaController(ManterMatriculaActor manter) {
        this.manter = manter;
    }

    @GetMapping
    public List<MatriculaRespostaDTO> listar() {
        return manter.listar();
    }

    @GetMapping("/{id}")
    public MatriculaRespostaDTO consultar(@PathVariable Long id) {
        return manter.consultar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MatriculaRespostaDTO criar(@Valid @RequestBody MatriculaRequisicaoDTO requisicao) {
        return manter.criar(requisicao);
    }

    @PutMapping("/{id}")
    public MatriculaRespostaDTO atualizar(
            @PathVariable Long id, @Valid @RequestBody MatriculaRequisicaoDTO requisicao) {
        return manter.atualizar(id, requisicao);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        manter.excluir(id);
    }
}
