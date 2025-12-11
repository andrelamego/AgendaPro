package com.lamego.agendapro.dto.command;

public record AtualizarProfissionalCommand(
        String nome,
        String telefone,
        String bio,
        Boolean aceitaNovosClientes,
        Boolean ativo
) {}