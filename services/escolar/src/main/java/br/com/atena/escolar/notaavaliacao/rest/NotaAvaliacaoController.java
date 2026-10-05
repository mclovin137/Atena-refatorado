package br.com.atena.escolar.notaavaliacao.rest;

import br.com.atena.escolar.notaavaliacao.actor.ManterNotaAvaliacaoActor;
import br.com.atena.escolar.notaavaliacao.dto.NotaAvaliacaoRequisicaoDTO;
import br.com.atena.escolar.notaavaliacao.dto.NotaAvaliacaoRespostaDTO;

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
@RequestMapping("/api/notas-avaliacoes")
public class NotaAvaliacaoController {

    private final ManterNotaAvaliacaoActor manter;

    public NotaAvaliacaoController(ManterNotaAvaliacaoActor manter) {
        this.manter = manter;
    }

    @GetMapping
    public List<NotaAvaliacaoRespostaDTO> listar() {
        return manter.listar();
    }

    @GetMapping("/{id}")
    public NotaAvaliacaoRespostaDTO consultar(@PathVariable Long id) {
        return manter.consultar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NotaAvaliacaoRespostaDTO criar(
            @Valid @RequestBody NotaAvaliacaoRequisicaoDTO requisicao) {
        return manter.criar(requisicao);
    }

    @PutMapping("/{id}")
    public NotaAvaliacaoRespostaDTO atualizar(
            @PathVariable Long id, @Valid @RequestBody NotaAvaliacaoRequisicaoDTO requisicao) {
        return manter.atualizar(id, requisicao);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        manter.excluir(id);
    }
}
