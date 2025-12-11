package com.lamego.agendapro.dto.agendamento.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record CriarAgendamentoRequest(
        @NotNull Long profissionalId,
        @NotNull Long servicoId,
        @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHoraInicio,
        String observacoes
) {}
