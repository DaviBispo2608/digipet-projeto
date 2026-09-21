package com.digipet.care.web;

import com.digipet.care.domain.ProfissionalServico;

public record ProfissionalServicoResponse(
        Long id,
        Long profissionalId,
        Long servicoId,
        String servicoNome,
        boolean habilitado) {

    public static ProfissionalServicoResponse from(ProfissionalServico ps) {
        return new ProfissionalServicoResponse(ps.getId(), ps.getUsuario().getId(), ps.getServico().getId(),
                ps.getServico().getNome(), ps.isHabilitado());
    }
}
