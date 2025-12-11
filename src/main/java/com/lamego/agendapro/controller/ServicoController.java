package com.lamego.agendapro.controller;

import com.lamego.agendapro.domain.model.Servico;
import com.lamego.agendapro.dto.servico.command.AtualizarServicoCommand;
import com.lamego.agendapro.dto.servico.command.CriarServicoCommand;
import com.lamego.agendapro.dto.servico.request.AtualizarServicoRequest;
import com.lamego.agendapro.dto.servico.request.CriarServicoRequest;
import com.lamego.agendapro.dto.servico.response.ServicoResponse;
import com.lamego.agendapro.service.interfaces.ServicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicos")
@RequiredArgsConstructor
public class ServicoController {

    private final ServicoService servicoService;

    @PostMapping
    public ServicoResponse criar(@RequestBody @Valid CriarServicoRequest request) {
        var command = new CriarServicoCommand(
                request.nome(),
                request.descricao(),
                request.duracaoMinutos(),
                request.preco()
        );

        Servico servico = servicoService.criarServico(command);
        return ServicoResponse.fromEntity(servico);
    }

    @PutMapping("/{id}")
    public ServicoResponse atualizar(@PathVariable Long id, @RequestBody @Valid AtualizarServicoRequest request) {
        var command = new AtualizarServicoCommand(
                request.nome(),
                request.descricao(),
                request.duracaoMinutos(),
                request.preco(),
                request.ativo()
        );
        Servico servico = servicoService.atualizarServico(id, command);
        return ServicoResponse.fromEntity(servico);
    }

    @GetMapping
    public List<ServicoResponse> listar(@RequestParam(required = false) String nome) {
        return servicoService.listarServicosAtivos(nome)
                .stream()
                .map(ServicoResponse::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public ServicoResponse buscarPorId(@PathVariable Long id) {
        Servico servico = servicoService.buscarPorId(id);
        return ServicoResponse.fromEntity(servico);
    }

    @DeleteMapping("/{id}")
    public void inativar(@PathVariable Long id) {
        servicoService.inativarServico(id);
    }
}
