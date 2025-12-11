package com.lamego.agendapro.service.interfaces;

import com.lamego.agendapro.domain.model.Disponibilidade;
import com.lamego.agendapro.dto.disponibilidade.command.AdicionarDisponibilidadeCommand;

import java.time.DayOfWeek;
import java.util.List;

public interface DisponibilidadeService {

    Disponibilidade adicionarDisponibilidade(AdicionarDisponibilidadeCommand command);

    void removerDisponibilidade(Long disponibilidadeId, Long profissionalId);

    List<Disponibilidade> listarPorProfissional(Long profissionalId);

    List<Disponibilidade> listarPorProfissionalEDia(Long profissionalId, DayOfWeek diaSemana);
}
