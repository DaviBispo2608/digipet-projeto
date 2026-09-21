package com.digipet.care.web;

import com.digipet.care.domain.Servico;
import com.digipet.care.enums.TipoServico;
import java.math.BigDecimal;

public record ServicoResponse(
        Long id,
        Long clinicaId,
        String nome,
        TipoServico tipo,
        Integer duracaoMinutos,
        String descricao,
        BigDecimal precoBase,
        boolean ativo) {

    public static ServicoResponse from(Servico servico) {
        return new ServicoResponse(servico.getId(), servico.getClinicaId(), servico.getNome(), servico.getTipo(),
                servico.getDuracaoMinutos(), servico.getDescricao(), servico.getPrecoBase(), servico.isAtivo());
    }
}
