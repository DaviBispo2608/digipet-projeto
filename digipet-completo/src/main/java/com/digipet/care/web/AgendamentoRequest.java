package com.digipet.care.web;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record AgendamentoRequest(
        @NotNull Long petId,
        @NotNull Long servicoId,
        @NotNull Long profissionalId,
        @NotNull @Future LocalDateTime dataHora,
        String observacoes) { }
