package com.lamego.agendapro.service.interfaces;

import com.lamego.agendapro.domain.model.Agendamento;
import com.lamego.agendapro.dto.agendamento.command.CriarAgendamentoCommand;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AgendamentoService {

    Agendamento criarAgendamento(CriarAgendamentoCommand command);

    void cancelarAgendamento(Long agendamentoId, Long usuarioId, String motivo);

    Agendamento confirmarAgendamento(Long agendamentoId, Long profissionalId);

    List<Agendamento> listarAgendamentosProfissionalNoDia(Long profissionalId, LocalDate dia);

    List<Agendamento> listarAgendamentosClienteFuturos(Long clienteId);

    List<LocalDateTime> listarHorariosDisponiveis(Long profissionalId, Long servicoId, LocalDate dia);
}
