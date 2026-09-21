package com.digipet.care.web;

import com.digipet.care.domain.Dono;

public record DonoResponse(
        Long id,
        Long clinicaId,
        String nome,
        String email,
        String telefone,
        String endereco) {

    public static DonoResponse from(Dono dono) {
        return new DonoResponse(dono.getId(), dono.getClinica().getId(), dono.getNome(), dono.getEmail(),
                dono.getTelefone(), dono.getEndereco());
    }
}
