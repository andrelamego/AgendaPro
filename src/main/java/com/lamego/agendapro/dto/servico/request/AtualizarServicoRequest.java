package com.lamego.agendapro.dto.servico.request;

import java.math.BigDecimal;

public record AtualizarServicoRequest(
        String nome,
        String descricao,
        Integer duracaoMinutos,
        BigDecimal preco,
        Boolean ativo
) {}
