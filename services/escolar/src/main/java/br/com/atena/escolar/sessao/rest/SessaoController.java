package br.com.atena.escolar.sessao.rest;

import br.com.atena.escolar.pessoa.dto.PessoaRespostaDTO;
import br.com.atena.escolar.sessao.actor.ManterSessaoActor;
import br.com.atena.escolar.sessao.dto.LoginRequisicaoDTO;
import br.com.atena.escolar.sessao.dto.PerfilRequisicaoDTO;
import br.com.atena.escolar.sessao.dto.SenhaRequisicaoDTO;
import br.com.atena.escolar.sessao.dto.SessaoRespostaDTO;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/session")
public class SessaoController {
    private final ManterSessaoActor manter;

    public SessaoController(ManterSessaoActor manter) {
        this.manter = manter;
    }

    @GetMapping
    public SessaoRespostaDTO consultar() {
        return manter.consultar();
    }

    @PostMapping("/login")
    public PessoaRespostaDTO entrar(@Valid @RequestBody LoginRequisicaoDTO requisicao) {
        return manter.entrar(requisicao);
    }

    @PostMapping("/logout")
    public void sair() {
        manter.sair();
    }

    @PutMapping("/profile")
    public PessoaRespostaDTO atualizarPerfil(@Valid @RequestBody PerfilRequisicaoDTO requisicao) {
        return manter.atualizarPerfil(requisicao);
    }

    @PutMapping("/password")
    public void atualizarSenha(@Valid @RequestBody SenhaRequisicaoDTO requisicao) {
        manter.atualizarSenha(requisicao);
    }
}
