package com.digipet.care.web;

import com.digipet.care.domain.Clinica;

public record ClinicaResponse(
        Long id,
        String razaoSocial,
        String nomeFantasia,
        String cnpj,
        String endereco,
        String telefone,
        String email) {

    public static ClinicaResponse from(Clinica clinica) {
        return new ClinicaResponse(clinica.getId(), clinica.getRazaoSocial(), clinica.getNomeFantasia(),
                clinica.getCnpj(), clinica.getEndereco(), clinica.getTelefone(), clinica.getEmail());
    }
}
