package com.lamego.agendapro.service.interfaces;

import com.lamego.agendapro.domain.model.Profissional;
import com.lamego.agendapro.dto.command.AtualizarProfissionalCommand;
import com.lamego.agendapro.dto.command.CriarProfissionalCommand;

import java.util.List;

public interface ProfissionalService {
    Profissional criarProfissional(CriarProfissionalCommand command);

    Profissional atualizarPerfil(Long profissionalId, AtualizarProfissionalCommand command);

    List<Profissional> listarAtivos(boolean apenasAceitandoNovos);

    Profissional buscarPorId(Long id);
}
