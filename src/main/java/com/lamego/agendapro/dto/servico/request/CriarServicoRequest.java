package com.lamego.agendapro.dto.servico.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record CriarServicoRequest(
        @NotBlank String nome,
        String descricao,
        @Min(1) Integer duracaoMinutos,
        @Min(0) BigDecimal preco
) {}
