package com.digipet.care.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DonoRequest(
        @NotBlank String nome,
        @NotBlank @Email String email,
        String telefone,
        String endereco) { }
