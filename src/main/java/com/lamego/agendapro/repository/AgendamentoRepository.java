package com.lamego.agendapro.repository;

import com.lamego.agendapro.domain.model.Agendamento;
import com.lamego.agendapro.domain.model.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    //Próximos agendamentos de um profissional (agenda do dia/semana)
    List<Agendamento> findByProfissionalIdAndDataHoraInicio(
            Long profissionalId,
            LocalDateTime inicio,
            LocalDateTime fim
    );

    //Próximos agendamentos de um cliente
    List<Agendamento> findByClienteIdAndDataHoraInicio(
            Long clienteId,
            LocalDateTime aPartirDe
    );

    //Agendamento de um cliente em um intervalo
    List<Agendamento> findByClienteIdAndDataHoraInicioBetween(
            Long clienteId,
            LocalDateTime inicio,
            LocalDateTime fim
    );

    // Verificar conflito de horario
    @Query("""
        select case when count(a) > 0 then true else false end
        from Agendamento a
        where a.profissional.id = :profissionalId
            and a.status not in (
                com.lamego.agendapro.domain.enums.AgendamentoStatus.CANCELADO_CLIENTE,
                com.lamego.agendapro.domain.enums.AgendamentoStatus.CANCELADO_PROFISSIONAL
            )
            and a.dataHoraInicio < :fim
            and a.dataHoraFim > :inicio
    """)
    boolean existsConflitoHorario(
            @Param("profissionalId") Long profissionalId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );

    // Buscar agendamentos futuros de um profissional
    List<Agendamento> findByProfissionalIdAndDataHoraInicioGreaterThanEqual(
            Long profissionalId,
            LocalDateTime aPartirDe
    );

    // Contar agendamentos por status em um intervalo
    @Query("""
        select a.status, count(a)
        from Agendamento a
        where a.dataHoraInicio between :inicio and :fim
        group by a.status
    """)
    List<Object[]> countByStatusEntreDatas(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );
}
