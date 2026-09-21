package com.digipet.care.web;

import com.digipet.care.domain.Escala;
import java.time.DayOfWeek;
import java.time.LocalTime;

public record EscalaResponse(
        Long id,
        Long profissionalId,
        DayOfWeek diaSemana,
        LocalTime horaInicio,
        LocalTime horaFim,
        boolean ativo) {

    public static EscalaResponse from(Escala escala) {
        return new EscalaResponse(escala.getId(), escala.getProfissional().getId(), escala.getDiaSemana(),
                escala.getHoraInicio(), escala.getHoraFim(), escala.isAtivo());
    }
}
