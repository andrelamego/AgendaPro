package com.lamego.agendapro.dto.user.response;

public record UserResponse(
        Long id,
        String nome,
        String email,
        String telefone
) {}