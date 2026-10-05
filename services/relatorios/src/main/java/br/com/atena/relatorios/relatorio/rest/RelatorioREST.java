package br.com.atena.relatorios.relatorio.rest;

import br.com.atena.relatorios.exportacao.actor.ExportarRelatorioActor;
import br.com.atena.relatorios.exportacao.dto.ArquivoRelatorioDTO;
import br.com.atena.relatorios.relatorio.actor.ConsultarRelatorioActor;
import br.com.atena.relatorios.relatorio.actor.IniciarRelatorioActor;
import br.com.atena.relatorios.relatorio.dto.RelatorioRequisicaoDTO;
import br.com.atena.relatorios.relatorio.dto.RelatorioRespostaDTO;

import jakarta.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
public class RelatorioREST {
    private final IniciarRelatorioActor inicio;
    private final ConsultarRelatorioActor consulta;
    private final ExportarRelatorioActor exportacao;

    public RelatorioREST(
            IniciarRelatorioActor inicio,
            ConsultarRelatorioActor consulta,
            ExportarRelatorioActor exportacao) {
        this.inicio = inicio;
        this.consulta = consulta;
        this.exportacao = exportacao;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RelatorioRespostaDTO iniciar(@Valid @RequestBody RelatorioRequisicaoDTO entrada) {
        return inicio.iniciar(entrada);
    }

    @GetMapping("/{id}")
    public RelatorioRespostaDTO consultar(@PathVariable Long id) {
        return consulta.consultar(id);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> baixar(
            @PathVariable Long id, @RequestParam(defaultValue = "pdf") String format)
            throws java.io.IOException {
        ArquivoRelatorioDTO arquivo = exportacao.exportar(id, format);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(arquivo.contentType()))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=atena-" + id + "." + format)
                .body(arquivo.conteudo());
    }
}
