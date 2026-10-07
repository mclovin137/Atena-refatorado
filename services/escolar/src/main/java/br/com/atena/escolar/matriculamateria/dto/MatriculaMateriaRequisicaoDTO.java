package br.com.atena.escolar.matriculamateria.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MatriculaMateriaRequisicaoDTO(@NotNull @Positive Long disciplinaId) {}
