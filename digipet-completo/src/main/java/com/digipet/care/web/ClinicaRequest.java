package com.digipet.care.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClinicaRequest(
        @NotBlank String razaoSocial,
        @NotBlank String nomeFantasia,
        @NotBlank String cnpj,
        String endereco,
        String telefone,
        @Email String email) { }
