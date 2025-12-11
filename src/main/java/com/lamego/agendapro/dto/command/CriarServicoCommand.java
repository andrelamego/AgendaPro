package com.lamego.agendapro.dto.command;

import java.math.BigDecimal;

public record CriarServicoCommand(
        String nome,
        String descricao,
        Integer duracaoMinutos,
        BigDecimal preco
) {}