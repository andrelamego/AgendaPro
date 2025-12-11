package com.lamego.agendapro.dto.disponibilidade.response;

import com.lamego.agendapro.domain.model.Disponibilidade;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record DisponibilidadeResponse(
        Long id,
        Long profissionalId,
        DayOfWeek diaSemana,
        LocalTime horaInicio,
        LocalTime horaFim
) {
    public static DisponibilidadeResponse fromEntity(Disponibilidade d) {
        return new DisponibilidadeResponse(
                d.getId(),
                d.getProfissional().getId(),
                d.getDiaSemana(),
                d.getHoraInicio(),
                d.getHoraFim()
        );
    }
}
