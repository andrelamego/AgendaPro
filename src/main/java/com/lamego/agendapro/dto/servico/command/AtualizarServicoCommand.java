package com.lamego.agendapro.dto.servico.command;

import java.math.BigDecimal;

public record AtualizarServicoCommand(
        String nome,
        String descricao,
        Integer duracaoMinutos,
        BigDecimal preco,
        Boolean ativo
) {}