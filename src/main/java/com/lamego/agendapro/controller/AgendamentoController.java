package com.lamego.agendapro.controller;

import com.lamego.agendapro.domain.model.Agendamento;
import com.lamego.agendapro.dto.agendamento.command.CriarAgendamentoCommand;
import com.lamego.agendapro.dto.agendamento.request.CancelarAgendamentoRequest;
import com.lamego.agendapro.dto.agendamento.request.CriarAgendamentoRequest;
import com.lamego.agendapro.dto.agendamento.response.AgendamentoResponse;
import com.lamego.agendapro.service.interfaces.AgendamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    @PostMapping
    public AgendamentoResponse criar(@RequestBody @Valid CriarAgendamentoRequest request) {
        var command = new CriarAgendamentoCommand(
                request.profissionalId(),
                request.clienteId(),
                request.servicoId(),
                request.dataHoraInicio(),
                request.observacoes()
        );

        Agendamento agendamento = agendamentoService.criarAgendamento(command);
        return AgendamentoResponse.fromEntity(agendamento);
    }

    @PostMapping("/{id}/cancelar")
    public void cancelar(@PathVariable Long id, @RequestBody @Valid CancelarAgendamentoRequest request) {
        agendamentoService.cancelarAgendamento(id, request.usuarioId(), request.motivo());
    }

    @PostMapping("/{id}/confirmar")
    public AgendamentoResponse confirmar(@PathVariable Long id, @RequestParam Long profissionalId) {
        Agendamento agendamento = agendamentoService.confirmarAgendamento(id, profissionalId);
        return AgendamentoResponse.fromEntity(agendamento);
    }

    @GetMapping("/profissional/{id}/dia/{data}")
    public List<AgendamentoResponse> listarPorProfissionalNoDia(
            @PathVariable Long id,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data
    ) {
        return agendamentoService.listarAgendamentosProfissionalNoDia(id, data)
                .stream()
                .map(AgendamentoResponse::fromEntity)
                .toList();
    }

    @GetMapping("/cliente/{id}/futuros")
    public List<AgendamentoResponse> listarFuturosClientes(@RequestParam Long id) {
        return agendamentoService.listarAgendamentosClienteFuturos(id)
                .stream()
                .map(AgendamentoResponse::fromEntity)
                .toList();
    }

    @GetMapping("/profissional/{id}/horarios-disponiveis")
    public List<LocalDateTime> listarHorariosDisponiveis(
            @PathVariable Long id,
            @RequestParam Long servicoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dia
    ) {
        return agendamentoService.listarHorariosDisponiveis(id, servicoId, dia);
    }
}
