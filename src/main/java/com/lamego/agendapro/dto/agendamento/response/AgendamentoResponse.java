package com.lamego.agendapro.dto.agendamento.response;

import com.lamego.agendapro.domain.model.Agendamento;

import java.time.LocalDateTime;

public record AgendamentoResponse(
        Long id,
        Long profissionalId,
        Long clienteId,
        Long servicoId,
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim,
        String status,
        String observacoes
) {
    public static AgendamentoResponse fromEntity(Agendamento a) {
        return new AgendamentoResponse(
                a.getId(),
                a.getProfissional().getId(),
                a.getCliente().getId(),
                a.getServico().getId(),
                a.getDataHoraInicio(),
                a.getDataHoraFim(),
                a.getStatus().name(),
                a.getObservacoes()
        );
    }
}
