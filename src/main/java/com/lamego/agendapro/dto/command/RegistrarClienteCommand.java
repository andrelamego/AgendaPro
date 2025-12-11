package com.lamego.agendapro.dto.command;

public record RegistrarClienteCommand(
        String nome,
        String email,
        String senha,
        String telefone
) {}
