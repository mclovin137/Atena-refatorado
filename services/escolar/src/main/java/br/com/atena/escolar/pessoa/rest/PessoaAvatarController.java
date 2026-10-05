package br.com.atena.escolar.pessoa.rest;

import br.com.atena.escolar.pessoa.actor.AvatarPessoaActor;
import br.com.atena.escolar.pessoa.dto.PessoaRespostaDTO;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class PessoaAvatarController {
    private final AvatarPessoaActor avatar;

    public PessoaAvatarController(AvatarPessoaActor avatar) {
        this.avatar = avatar;
    }

    @PostMapping(value = "/api/pessoas/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PessoaRespostaDTO enviar(@PathVariable Long id, @RequestParam MultipartFile file)
            throws IOException {
        return avatar.enviar(id, file.getOriginalFilename(), file.getBytes());
    }

    @GetMapping("/api/pessoas/{id}/avatar")
    public ResponseEntity<byte[]> consultar(@PathVariable Long id) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CACHE_CONTROL, "private, no-store")
                .body(avatar.consultar(id));
    }
}
