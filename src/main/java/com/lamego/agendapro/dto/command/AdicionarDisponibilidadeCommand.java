package com.lamego.agendapro.dto.command;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record AdicionarDisponibilidadeCommand(
        Long profissionalId,
        DayOfWeek diaSemana,
        LocalTime horaInicio,
        LocalTime horaFim
) {}