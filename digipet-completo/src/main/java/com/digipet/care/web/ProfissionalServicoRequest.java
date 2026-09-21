package com.digipet.care.web;

import jakarta.validation.constraints.NotNull;

public record ProfissionalServicoRequest(@NotNull Long servicoId) { }
