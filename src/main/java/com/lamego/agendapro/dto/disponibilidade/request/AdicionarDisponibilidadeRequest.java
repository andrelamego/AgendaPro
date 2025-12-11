package com.lamego.agendapro.dto.disponibilidade.request;

import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record AdicionarDisponibilidadeRequest(
        @NotNull Long profissionalId,
        @NotNull DayOfWeek diaSemana,
        @NotNull LocalTime horaInicio,
        @NotNull LocalTime horaFim
) {}