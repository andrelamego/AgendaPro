package com.lamego.agendapro.repository;

import com.lamego.agendapro.domain.model.Disponibilidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public interface DisponibilidadeRepository extends JpaRepository<Disponibilidade, Long> {

    // Todas as disponibilidades de um profissional
    List<Disponibilidade> findByProfissionalIdOrderByDiaSemanaAscHoraInicioAsc(Long profissionalId);

    // Disponibilidades de um profissional por dia da semana
    List<Disponibilidade> findByProfissionalIdAndDiaSemanaOrderByHoraInicioAsc(Long profissionalId, DayOfWeek diaSemana);

    // Ver se já existe disponibilidade igual
    boolean existsByProfissionalIdAndDiaSemanaAndHoraInicioAndHoraFim(
            Long profissionalId,
            DayOfWeek diaSemana,
            LocalTime inicio,
            LocalTime fim
    );
}
