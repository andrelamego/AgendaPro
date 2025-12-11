package com.lamego.agendapro.dto.profissional.response;

import com.lamego.agendapro.domain.model.Profissional;

public record ProfissionalResponse(
        Long id,
        Long usuarioId,
        String nome,
        String email,
        String telefone,
        String bio,
        boolean aceitaNovosClientes,
        boolean ativo
) {
    public static ProfissionalResponse fromEntity(Profissional p) {
        var u = p.getUsuario();
        return new ProfissionalResponse(
                p.getId(),
                u.getId(),
                u.getNome(),
                u.getEmail(),
                u.getTelefone(),
                p.getBio(),
                p.getAceitaNovosClientes(),
                p.getAtivo()
        );
    }
}
