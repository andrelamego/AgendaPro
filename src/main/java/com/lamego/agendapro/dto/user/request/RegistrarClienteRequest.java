package com.lamego.agendapro.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegistrarClienteRequest(
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank String senha,
        String telefone
) {}
