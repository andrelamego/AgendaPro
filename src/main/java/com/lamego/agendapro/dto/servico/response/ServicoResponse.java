package com.lamego.agendapro.dto.servico.response;

import com.lamego.agendapro.domain.model.Servico;

import java.math.BigDecimal;

public record ServicoResponse(
        Long id,
        String nome,
        String descricao,
        Integer duracaoMinutos,
        BigDecimal preco,
        boolean ativo
) {
    public static ServicoResponse fromEntity(Servico s) {
        return new ServicoResponse(
                s.getId(),
                s.getNome(),
                s.getDescricao(),
                s.getDuracaoMinutos(),
                s.getPreco(),
                s.getAtivo()
        );
    }
}
