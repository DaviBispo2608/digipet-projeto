package com.digipet.care.web;

import com.digipet.care.domain.Agendamento;
import com.digipet.care.enums.StatusAgendamento;
import java.time.LocalDateTime;

public record AgendamentoResponse(
        Long id,
        Long clinicaId,
        Long petId,
        String petNome,
        Long servicoId,
        String servicoNome,
        Long profissionalId,
        String profissionalNome,
        LocalDateTime dataHora,
        LocalDateTime dataHoraFim,
        StatusAgendamento status,
        String observacoes) {

    public static AgendamentoResponse from(Agendamento agendamento) {
        return new AgendamentoResponse(
                agendamento.getId(),
                agendamento.getClinicaId(),
                agendamento.getPet().getId(),
                agendamento.getPet().getNome(),
                agendamento.getServico().getId(),
                agendamento.getServico().getNome(),
                agendamento.getProfissional().getId(),
                agendamento.getProfissional().getNomeCompleto(),
                agendamento.getDataHora(),
                agendamento.getDataHoraFim(),
                agendamento.getStatus(),
                agendamento.getObservacoes());
    }
}
