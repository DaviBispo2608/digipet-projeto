package com.digipet.care.web;

import com.digipet.care.enums.TipoServico;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record ServicoRequest(
        @NotBlank String nome,
        @NotNull TipoServico tipo,
        @NotNull @Positive Integer duracaoMinutos,
        String descricao,
        @PositiveOrZero BigDecimal precoBase) { }
