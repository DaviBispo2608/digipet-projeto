package com.digipet.care.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;

public record PetRequest(
        @NotBlank String nome,
        String especie,
        String raca,
        @PositiveOrZero BigDecimal peso,
        @Past LocalDate nascimento) { }
