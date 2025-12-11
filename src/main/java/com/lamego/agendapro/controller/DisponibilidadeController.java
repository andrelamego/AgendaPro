package com.lamego.agendapro.controller;

import com.lamego.agendapro.domain.model.Disponibilidade;
import com.lamego.agendapro.domain.model.Profissional;
import com.lamego.agendapro.dto.disponibilidade.command.AdicionarDisponibilidadeCommand;
import com.lamego.agendapro.dto.disponibilidade.request.AdicionarDisponibilidadeRequest;
import com.lamego.agendapro.dto.disponibilidade.response.DisponibilidadeResponse;
import com.lamego.agendapro.security.SecurityUtils;
import com.lamego.agendapro.service.interfaces.DisponibilidadeService;
import com.lamego.agendapro.service.interfaces.ProfissionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disponibilidades")
@RequiredArgsConstructor
public class DisponibilidadeController {

    private final DisponibilidadeService disponibilidadeService;
    private final ProfissionalService profissionalService;

    @PostMapping
    public DisponibilidadeResponse adicionar(@RequestBody @Valid AdicionarDisponibilidadeRequest request) {
        String email = SecurityUtils.getCurrentUsername();
        Profissional profissional = profissionalService.buscarPorEmail(email);

        var command = new AdicionarDisponibilidadeCommand(
                profissional.getId(),
                request.diaSemana(),
                request.horaInicio(),
                request.horaFim()
        );

        Disponibilidade disp = disponibilidadeService.adicionarDisponibilidade(command);
        return DisponibilidadeResponse.fromEntity(disp);
    }

    @DeleteMapping("/{id}")
    public void remover(@PathVariable Long id) {
        String email = SecurityUtils.getCurrentUsername();
        Profissional profissional = profissionalService.buscarPorEmail(email);
        disponibilidadeService.removerDisponibilidade(id, profissional.getId());
    }

    @GetMapping("/me")
    public List<DisponibilidadeResponse> listarMinhasDisponibilidades() {
        String email = SecurityUtils.getCurrentUsername();
        Profissional profissional = profissionalService.buscarPorEmail(email);

        return disponibilidadeService.listarPorProfissional(profissional.getId())
                .stream()
                .map(DisponibilidadeResponse::fromEntity)
                .toList();
    }
}
