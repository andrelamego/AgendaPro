package com.lamego.agendapro.controller;

import com.lamego.agendapro.domain.model.Profissional;
import com.lamego.agendapro.dto.profissional.command.AtualizarProfissionalCommand;
import com.lamego.agendapro.dto.profissional.command.CriarProfissionalCommand;
import com.lamego.agendapro.dto.profissional.request.AtualizarProfissionalRequest;
import com.lamego.agendapro.dto.profissional.request.CriarProfissionalRequest;
import com.lamego.agendapro.dto.profissional.response.ProfissionalResponse;
import com.lamego.agendapro.service.interfaces.ProfissionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profissionais")
@RequiredArgsConstructor
public class ProfissionalController {

    private final ProfissionalService profissionalService;

    @PostMapping
    public ProfissionalResponse criar(@RequestBody @Valid CriarProfissionalRequest request) {
        var command = new CriarProfissionalCommand(
                request.nome(),
                request.email(),
                request.senha(),
                request.telefone(),
                request.bio(),
                request.aceitaNovosClientes()
        );

        Profissional profissional = profissionalService.criarProfissional(command);
        return ProfissionalResponse.fromEntity(profissional);
    }

    @PutMapping("/{id}")
    public ProfissionalResponse atualizar(
            @PathVariable Long id,
            @RequestBody AtualizarProfissionalRequest request
    ) {
        var command = new AtualizarProfissionalCommand(
                request.nome(),
                request.telefone(),
                request.bio(),
                request.aceitaNovosClientes(),
                request.ativo()
        );

        Profissional atualizado = profissionalService.atualizarPerfil(id, command);
        return ProfissionalResponse.fromEntity(atualizado);
    }

    @GetMapping
    public List<ProfissionalResponse> listar(@RequestParam(defaultValue = "false") boolean apenasAceitandoNovos) {
        return profissionalService.listarAtivos(apenasAceitandoNovos)
                .stream()
                .map(ProfissionalResponse::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public ProfissionalResponse buscarPorId(@PathVariable Long id) {
        Profissional profissional = profissionalService.buscarPorId(id);
        return ProfissionalResponse.fromEntity(profissional);
    }
}
