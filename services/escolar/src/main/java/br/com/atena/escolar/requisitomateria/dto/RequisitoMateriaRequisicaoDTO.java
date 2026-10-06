package br.com.atena.escolar.requisitomateria.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RequisitoMateriaRequisicaoDTO(@NotNull @Positive Long materiaId) {}
