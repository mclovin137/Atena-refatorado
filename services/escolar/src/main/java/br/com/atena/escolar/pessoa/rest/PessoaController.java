package br.com.atena.escolar.pessoa.rest;

import br.com.atena.escolar.pessoa.actor.ManterPessoaActor;
import br.com.atena.escolar.pessoa.dto.CadastroPessoaRequisicaoDTO;
import br.com.atena.escolar.pessoa.dto.PessoaRespostaDTO;

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
@RequestMapping("/api/pessoas")
public class PessoaController {
    private final ManterPessoaActor manter;

    public PessoaController(ManterPessoaActor manter) {
        this.manter = manter;
    }

    @GetMapping
    public List<PessoaRespostaDTO> listar() {
        return manter.listar();
    }

    @GetMapping("/{id}")
    public PessoaRespostaDTO consultar(@PathVariable Long id) {
        return manter.consultar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PessoaRespostaDTO criar(@Valid @RequestBody CadastroPessoaRequisicaoDTO requisicao) {
        return manter.criar(requisicao);
    }

    @PutMapping("/{id}")
    public PessoaRespostaDTO atualizar(
            @PathVariable Long id, @Valid @RequestBody CadastroPessoaRequisicaoDTO requisicao) {
        return manter.atualizar(id, requisicao);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        manter.excluir(id);
    }
}
