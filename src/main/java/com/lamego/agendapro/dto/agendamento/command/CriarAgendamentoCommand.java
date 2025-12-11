package com.lamego.agendapro.dto.agendamento.command;

import java.time.LocalDateTime;

public record CriarAgendamentoCommand(
        Long profissionalId,
        Long clienteId,
        Long servicoId,
        LocalDateTime dataHoraInicio,
        String observacoes
) {}