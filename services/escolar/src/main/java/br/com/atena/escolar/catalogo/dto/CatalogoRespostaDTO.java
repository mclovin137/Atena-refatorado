package br.com.atena.escolar.catalogo.dto;

import br.com.atena.escolar.curso.dto.CursoRespostaDTO;
import br.com.atena.escolar.materia.dto.MateriaRespostaDTO;
import br.com.atena.escolar.serie.dto.SerieRespostaDTO;

import java.util.List;

public record CatalogoRespostaDTO(
        List<CursoRespostaDTO> cursos,
        List<SerieRespostaDTO> series,
        List<MateriaRespostaDTO> materias) {}
