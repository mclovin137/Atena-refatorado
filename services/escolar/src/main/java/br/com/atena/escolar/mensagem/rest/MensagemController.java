package br.com.atena.escolar.mensagem.rest;

import br.com.atena.escolar.mensagem.actor.ManterMensagemActor;
import br.com.atena.escolar.mensagem.dto.MensagemRequisicaoDTO;
import br.com.atena.escolar.mensagem.dto.MensagemRespostaDTO;

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
@RequestMapping("/api/mensagens")
public class MensagemController {

    private final ManterMensagemActor manter;

    public MensagemController(ManterMensagemActor manter) {
        this.manter = manter;
    }

    @GetMapping
    public List<MensagemRespostaDTO> listar() {
        return manter.listar();
    }

    @GetMapping("/{id}")
    public MensagemRespostaDTO consultar(@PathVariable Long id) {
        return manter.consultar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MensagemRespostaDTO criar(@Valid @RequestBody MensagemRequisicaoDTO requisicao) {
        return manter.criar(requisicao);
    }

    @PutMapping("/{id}")
    public MensagemRespostaDTO atualizar(
            @PathVariable Long id, @Valid @RequestBody MensagemRequisicaoDTO requisicao) {
        return manter.atualizar(id, requisicao);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        manter.excluir(id);
    }
}
