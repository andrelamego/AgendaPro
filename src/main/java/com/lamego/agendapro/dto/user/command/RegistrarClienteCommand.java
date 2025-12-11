package com.lamego.agendapro.dto.user.command;

public record RegistrarClienteCommand(
        String nome,
        String email,
        String senha,
        String telefone
) {}
