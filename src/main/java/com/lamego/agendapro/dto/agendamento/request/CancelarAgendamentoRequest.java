package com.lamego.agendapro.dto.agendamento.request;

import jakarta.validation.constraints.NotNull;

public record CancelarAgendamentoRequest(
        @NotNull Long usuarioId,
        String motivo
) {}
