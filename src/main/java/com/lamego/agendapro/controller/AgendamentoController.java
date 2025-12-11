package com.lamego.agendapro.controller;

import com.lamego.agendapro.domain.model.Agendamento;
import com.lamego.agendapro.domain.model.Profissional;
import com.lamego.agendapro.domain.model.User;
import com.lamego.agendapro.dto.agendamento.command.CriarAgendamentoCommand;
import com.lamego.agendapro.dto.agendamento.request.CancelarAgendamentoRequest;
import com.lamego.agendapro.dto.agendamento.request.CriarAgendamentoRequest;
import com.lamego.agendapro.dto.agendamento.response.AgendamentoResponse;
import com.lamego.agendapro.security.SecurityUtils;
import com.lamego.agendapro.service.interfaces.AgendamentoService;
import com.lamego.agendapro.service.interfaces.ProfissionalService;
import com.lamego.agendapro.service.interfaces.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
    private final UserService userService;
    private final ProfissionalService profissionalService;

    // CLIENTE cria agendamento
    @PostMapping
    public AgendamentoResponse criar(@RequestBody @Valid CriarAgendamentoRequest request) {
        String email = SecurityUtils.getCurrentUsername();
        User cliente = userService.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado"));

        var command = new CriarAgendamentoCommand(
                request.profissionalId(),
                cliente.getId(),
                request.servicoId(),
                request.dataHoraInicio(),
                request.observacoes()
        );

        Agendamento agendamento = agendamentoService.criarAgendamento(command);
        return AgendamentoResponse.fromEntity(agendamento);
    }

    // CLIENTE ou PROFISSIONAL cancela
    @PostMapping("/{id}/cancelar")
    public void cancelar(@PathVariable Long id,
                         @RequestBody @Valid CancelarAgendamentoRequest request) {
        String email = SecurityUtils.getCurrentUsername();
        User usuario = userService.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado"));

        agendamentoService.cancelarAgendamento(id, usuario.getId(), request.motivo());
    }

    // PROFISSIONAL confirma
    @PostMapping("/{id}/confirmar")
    public AgendamentoResponse confirmar(@PathVariable Long id) {
        String email = SecurityUtils.getCurrentUsername();
        Profissional profissional = profissionalService.buscarPorEmail(email);

        Agendamento agendamento = agendamentoService.confirmarAgendamento(id, profissional.getId());
        return AgendamentoResponse.fromEntity(agendamento);
    }

    // PROFISSIONAL vê agenda do dia
    @GetMapping("/me/profissional/dia/{data}")
    public List<AgendamentoResponse> listarPorProfissionalNoDia(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data
    ) {
        String email = SecurityUtils.getCurrentUsername();
        Profissional profissional = profissionalService.buscarPorEmail(email);

        return agendamentoService.listarAgendamentosProfissionalNoDia(profissional.getId(), data)
                .stream()
                .map(AgendamentoResponse::fromEntity)
                .toList();
    }

    // CLIENTE vê agendamentos futuros
    @GetMapping("/me/cliente/futuros")
    public List<AgendamentoResponse> listarFuturosCliente() {
        String email = SecurityUtils.getCurrentUsername();
        User cliente = userService.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado"));

        return agendamentoService.listarAgendamentosClienteFuturos(cliente.getId())
                .stream()
                .map(AgendamentoResponse::fromEntity)
                .toList();
    }

    // Horários disponíveis para um profissional+serviço em um dia
    @GetMapping("/profissional/{id}/horarios-disponiveis")
    public List<LocalDateTime> listarHorariosDisponiveis(
            @PathVariable Long id,
            @RequestParam Long servicoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data
    ) {
        return agendamentoService.listarHorariosDisponiveis(id, servicoId, data);
    }
}
