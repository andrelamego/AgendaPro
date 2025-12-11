package com.lamego.agendapro.controller;

import com.lamego.agendapro.domain.model.Disponibilidade;
import com.lamego.agendapro.dto.disponibilidade.command.AdicionarDisponibilidadeCommand;
import com.lamego.agendapro.dto.disponibilidade.request.AdicionarDisponibilidadeRequest;
import com.lamego.agendapro.dto.disponibilidade.response.DisponibilidadeResponse;
import com.lamego.agendapro.service.interfaces.DisponibilidadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disponibilidades")
@RequiredArgsConstructor
public class DisponibilidadeController {

    private final DisponibilidadeService disponibilidadeService;

    @PostMapping
    public DisponibilidadeResponse adicionar(@RequestBody @Valid AdicionarDisponibilidadeRequest request) {
        var command = new AdicionarDisponibilidadeCommand(
                request.profissionalId(),
                request.diaSemana(),
                request.horaInicio(),
                request.horaFim()
        );

        Disponibilidade disponibilidade = disponibilidadeService.adicionarDisponibilidade(command);
        return DisponibilidadeResponse.fromEntity(disponibilidade);
    }

    @DeleteMapping("/{id}")
    public void remover(@PathVariable Long id, @RequestParam Long profissionalId) {
        disponibilidadeService.removerDisponibilidade(id, profissionalId);
    }

    @GetMapping("/profissional/id")
    public List<DisponibilidadeResponse> listarPorProfissional(@RequestParam Long profissionalId) {
        return disponibilidadeService.listarPorProfissional(profissionalId)
                .stream()
                .map(DisponibilidadeResponse::fromEntity)
                .toList();
    }
}
