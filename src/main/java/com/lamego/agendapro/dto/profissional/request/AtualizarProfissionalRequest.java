package com.lamego.agendapro.dto.profissional.request;

public record AtualizarProfissionalRequest(
        String nome,
        String telefone,
        String bio,
        Boolean aceitaNovosClientes,
        Boolean ativo
) {}
