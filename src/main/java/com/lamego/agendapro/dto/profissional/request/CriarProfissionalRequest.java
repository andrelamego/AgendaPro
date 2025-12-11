package com.lamego.agendapro.dto.profissional.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CriarProfissionalRequest(
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank String senha,
        String telefone,
        String bio,
        boolean aceitaNovosClientes
) {}
