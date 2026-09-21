package com.digipet.care.web;

import com.digipet.care.domain.Usuario;
import com.digipet.care.enums.Perfil;

public record UsuarioResponse(
        Long id,
        Long clinicaId,
        String nomeCompleto,
        String email,
        Perfil perfil,
        String crmv,
        boolean ativo) {

    public static UsuarioResponse from(Usuario usuario) {
        Long clinicaId = usuario.getClinica() != null ? usuario.getClinica().getId() : null;
        return new UsuarioResponse(usuario.getId(), clinicaId, usuario.getNomeCompleto(), usuario.getEmail(),
                usuario.getPerfil(), usuario.getCrmv(), usuario.isAtivo());
    }
}
