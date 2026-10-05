package br.com.atena.escolar.catalogo.rest;

import br.com.atena.escolar.catalogo.actor.ConsultarCatalogoActor;
import br.com.atena.escolar.catalogo.dto.CatalogoRespostaDTO;
import br.com.atena.escolar.catalogo.dto.PessoaDiretorioRespostaDTO;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CatalogoController {
    private final ConsultarCatalogoActor consultar;

    public CatalogoController(ConsultarCatalogoActor consultar) {
        this.consultar = consultar;
    }

    @GetMapping("/api/catalogos")
    public CatalogoRespostaDTO catalogos() {
        return consultar.catalogos();
    }

    @GetMapping("/api/directory")
    public List<PessoaDiretorioRespostaDTO> diretorio() {
        return consultar.diretorio();
    }
}
