package com.lamego.agendapro.dto.profissional.command;

public record CriarProfissionalCommand(
        String nome,
        String email,
        String senha,
        String telefone,
        String bio,
        boolean aceitaNovosClientes
) {}