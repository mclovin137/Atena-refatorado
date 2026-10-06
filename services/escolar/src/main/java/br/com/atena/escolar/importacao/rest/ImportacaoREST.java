package br.com.atena.escolar.importacao.rest;

import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.importacao.actor.ManterImportacaoActor;
import br.com.atena.escolar.importacao.dto.ImportacaoRespostaDTO;
import br.com.atena.escolar.importacao.dto.VincularPessoaRequisicaoDTO;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/importacoes")
public class ImportacaoREST {
    private final ManterImportacaoActor manter;

    public ImportacaoREST(ManterImportacaoActor manter) {
        this.manter = manter;
    }

    @PostMapping(value = "/analisar", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ImportacaoRespostaDTO analisar(@RequestParam("file") MultipartFile arquivo) {
        if (arquivo.getSize() > 10 * 1024 * 1024) {
            throw ErroAplicacao.entrada("Arquivo deve conter até 10 MiB");
        }
        try {
            return manter.analisar(arquivo.getBytes());
        } catch (IOException erro) {
            throw ErroAplicacao.entrada("Arquivo não pôde ser lido");
        }
    }

    @GetMapping("/{id}")
    public ImportacaoRespostaDTO consultar(@PathVariable UUID id) {
        return manter.consultar(id);
    }

    @PostMapping("/{id}/aplicar")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ImportacaoRespostaDTO aplicar(@PathVariable UUID id) {
        return manter.aplicar(id);
    }

    @PostMapping("/{id}/retomar")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ImportacaoRespostaDTO retomar(@PathVariable UUID id) {
        return manter.retomar(id);
    }

    @PostMapping("/{id}/reprocessar")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ImportacaoRespostaDTO reprocessar(@PathVariable UUID id) {
        return manter.reprocessar(id);
    }

    @PostMapping("/{id}/vinculos-pessoas")
    public ImportacaoRespostaDTO vincular(
            @PathVariable UUID id, @Valid @RequestBody VincularPessoaRequisicaoDTO requisicao) {
        return manter.vincular(id, requisicao);
    }
}
