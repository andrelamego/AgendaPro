package com.lamego.agendapro.dto.command;

public record CriarProfissionalCommand(
        String nome,
        String email,
        String senha,
        String telefone,
        String bio,
        boolean aceitaNovosClientes
) {}