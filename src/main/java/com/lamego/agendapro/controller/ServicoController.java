package com.lamego.agendapro.controller;

import com.lamego.agendapro.domain.model.Profissional;
import com.lamego.agendapro.domain.model.Servico;
import com.lamego.agendapro.dto.servico.command.AtualizarServicoCommand;
import com.lamego.agendapro.dto.servico.command.CriarServicoCommand;
import com.lamego.agendapro.dto.servico.request.AtualizarServicoRequest;
import com.lamego.agendapro.dto.servico.request.CriarServicoRequest;
import com.lamego.agendapro.dto.servico.response.ServicoResponse;
import com.lamego.agendapro.security.SecurityUtils;
import com.lamego.agendapro.service.interfaces.ProfissionalService;
import com.lamego.agendapro.service.interfaces.ServicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicos")
@RequiredArgsConstructor
public class ServicoController {

    private final ServicoService servicoService;
    private final ProfissionalService profissionalService;

    // PROFISSIONAL logado cria serviço
    @PreAuthorize("hasRole('PROFISSIONAL')")
    @PostMapping
    public ServicoResponse criar(@RequestBody @Valid CriarServicoRequest request) {
        String email = SecurityUtils.getCurrentUsername();
        Profissional profissional = profissionalService.buscarPorEmail(email);

        var command = new CriarServicoCommand(
                request.nome(),
                request.descricao(),
                request.duracaoMinutos(),
                request.preco()
        );

        Servico servico = servicoService.criarServico(profissional.getId(), command);
        return ServicoResponse.fromEntity(servico);
    }

    // PROFISSIONAL logado atualiza um de seus serviços
    @PreAuthorize("hasRole('PROFISSIONAL')")
    @PutMapping("/{id}")
    public ServicoResponse atualizar(@PathVariable Long id,
                                     @RequestBody AtualizarServicoRequest request) {
        String email = SecurityUtils.getCurrentUsername();
        Profissional profissional = profissionalService.buscarPorEmail(email);

        var command = new AtualizarServicoCommand(
                request.nome(),
                request.descricao(),
                request.duracaoMinutos(),
                request.preco(),
                request.ativo()
        );

        Servico servico = servicoService.atualizarServico(profissional.getId(), id, command);
        return ServicoResponse.fromEntity(servico);
    }

    // PROFISSIONAL logado inativa serviço próprio
    @PreAuthorize("hasRole('PROFISSIONAL')")
    @DeleteMapping("/{id}")
    public void inativar(@PathVariable Long id) {
        String email = SecurityUtils.getCurrentUsername();
        Profissional profissional = profissionalService.buscarPorEmail(email);

        servicoService.inativarServico(profissional.getId(), id);
    }

    // PROFISSIONAL logado lista seus serviços
    @PreAuthorize("hasRole('PROFISSIONAL')")
    @GetMapping("/me")
    public List<ServicoResponse> listarMeusServicos(
            @RequestParam(required = false) String nome
    ) {
        String email = SecurityUtils.getCurrentUsername();
        Profissional profissional = profissionalService.buscarPorEmail(email);

        return servicoService.listarServicosDoProfissional(profissional.getId(), nome)
                .stream()
                .map(ServicoResponse::fromEntity)
                .toList();
    }

    // CLIENTE (ou público) lista serviços de um profissional específico
    @GetMapping("/profissional/{profissionalId}")
    public List<ServicoResponse> listarPorProfissional(@PathVariable Long profissionalId) {
        return servicoService.listarServicosDoProfissionalPublic(profissionalId)
                .stream()
                .map(ServicoResponse::fromEntity)
                .toList();
    }

    // opcional: detalhe de um serviço de um profissional (público)
    @GetMapping("/profissional/{profissionalId}/{servicoId}")
    public ServicoResponse buscarServicoDeProfissional(@PathVariable Long profissionalId,
                                                       @PathVariable Long servicoId) {
        Servico servico = servicoService.buscarPorIdEProfissional(profissionalId, servicoId);
        return ServicoResponse.fromEntity(servico);
    }
}
