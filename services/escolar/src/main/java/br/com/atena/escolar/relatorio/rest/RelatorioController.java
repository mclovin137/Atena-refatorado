package br.com.atena.escolar.relatorio.rest;

import br.com.atena.escolar.relatorio.actor.ManterRelatorioActor;
import br.com.atena.escolar.relatorio.dto.RelatorioRespostaDTO;
import br.com.atena.escolar.relatorio.dto.SolicitarRelatorioDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {
    private final ManterRelatorioActor actor;

    public RelatorioController(ManterRelatorioActor actor) {
        this.actor = actor;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RelatorioRespostaDTO iniciar(@Valid @RequestBody SolicitarRelatorioDTO solicitacao) {
        return actor.iniciar(solicitacao);
    }

    @GetMapping("/{id}")
    public RelatorioRespostaDTO consultar(@PathVariable Long id) {
        return actor.consultar(id);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> baixar(
            @PathVariable Long id,
            @RequestParam(defaultValue = "pdf") @Pattern(regexp = "pdf|csv|json") String format) {
        var documento = actor.baixar(id, format);
        return ResponseEntity.ok()
                .header("Content-Type", documento.tipoConteudo())
                .header("Content-Disposition", documento.disposicao())
                .body(documento.conteudo());
    }
}
