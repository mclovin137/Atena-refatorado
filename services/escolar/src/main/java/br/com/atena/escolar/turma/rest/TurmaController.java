package br.com.atena.escolar.turma.rest;

import br.com.atena.escolar.turma.actor.ManterTurmaActor;
import br.com.atena.escolar.turma.dto.TurmaRequisicaoDTO;
import br.com.atena.escolar.turma.dto.TurmaRespostaDTO;

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
@RequestMapping("/api/turmas")
public class TurmaController {

    private final ManterTurmaActor manter;

    public TurmaController(ManterTurmaActor manter) {
        this.manter = manter;
    }

    @GetMapping
    public List<TurmaRespostaDTO> listar() {
        return manter.listar();
    }

    @GetMapping("/{id}")
    public TurmaRespostaDTO consultar(@PathVariable Long id) {
        return manter.consultar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TurmaRespostaDTO criar(@Valid @RequestBody TurmaRequisicaoDTO requisicao) {
        return manter.criar(requisicao);
    }

    @PutMapping("/{id}")
    public TurmaRespostaDTO atualizar(
            @PathVariable Long id, @Valid @RequestBody TurmaRequisicaoDTO requisicao) {
        return manter.atualizar(id, requisicao);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        manter.excluir(id);
    }
}
