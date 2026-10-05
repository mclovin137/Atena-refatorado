package br.com.atena.escolar.material.rest;

import br.com.atena.escolar.material.actor.ManterMaterialActor;
import br.com.atena.escolar.material.dto.MaterialRespostaDTO;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/materiais")
public class MaterialController {
    private final ManterMaterialActor manter;

    public MaterialController(ManterMaterialActor manter) {
        this.manter = manter;
    }

    @GetMapping
    public List<MaterialRespostaDTO> listar() {
        return manter.listar();
    }

    @GetMapping("/{id}")
    public MaterialRespostaDTO consultar(@PathVariable Long id) {
        return manter.consultar(id);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public MaterialRespostaDTO enviar(
            @RequestParam Long disciplinaId,
            @RequestParam String nome,
            @RequestParam MultipartFile file)
            throws IOException {
        return manter.enviar(disciplinaId, nome, file.getOriginalFilename(), file.getBytes());
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> baixar(@PathVariable Long id) {
        var arquivo = manter.baixar(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(arquivo.tipo()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + arquivo.nome())
                .body(arquivo.bytes());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        manter.excluir(id);
    }
}
