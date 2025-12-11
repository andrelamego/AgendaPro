package com.lamego.agendapro.service.implementation;

import com.lamego.agendapro.domain.enums.AgendamentoStatus;
import com.lamego.agendapro.domain.model.Agendamento;
import com.lamego.agendapro.domain.model.Profissional;
import com.lamego.agendapro.domain.model.Servico;
import com.lamego.agendapro.domain.model.User;
import com.lamego.agendapro.dto.agendamento.command.CriarAgendamentoCommand;
import com.lamego.agendapro.repository.*;
import com.lamego.agendapro.service.interfaces.AgendamentoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AgendamentoServiceImpl implements AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final ProfissionalRepository profissionalRepository;
    private final UserRepository userRepository;
    private final ServicoRepository servicoRepository;
    private final DisponibilidadeRepository disponibilidadeRepository;

    @Override
    public Agendamento criarAgendamento(CriarAgendamentoCommand command) {
        Profissional profissional = profissionalRepository.findById(command.profissionalId())
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado!"));

        User cliente = userRepository.findById(command.clienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado!"));

        Servico servico = servicoRepository.findById(command.servicoId())
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado!"));

        if (!servico.getProfissional().getId().equals(profissional.getId())) {
            throw new IllegalArgumentException("Serviço não pertence ao profissional selecionado.");
        }

        LocalDateTime inicio = command.dataHoraInicio();
        LocalDateTime fim = inicio.plusMinutes(servico.getDuracaoMinutos());

        // valida se horario está dentro da disponibilidade
        validarHorarioDentroDaDisponibilidade(profissional, inicio, fim);

        // valida se não há conflito com algum outro agendamento
        boolean existeConflito = agendamentoRepository.existsConflitoHorario(
                profissional.getId(),
                inicio,
                fim
        );

        if(existeConflito) {
            throw new IllegalArgumentException("Já existe um agendamento nesse horário para o profissional selecionado");
        }

        // cria o agendamento
        Agendamento agendamento = Agendamento.builder()
                .profissional(profissional)
                .cliente(cliente)
                .servico(servico)
                .dataHoraInicio(inicio)
                .dataHoraFim(fim)
                .status(AgendamentoStatus.AGENDADO)
                .observacoes(command.observacoes())
                .criadoEm(LocalDateTime.now())
                .build();

        return agendamentoRepository.save(agendamento);
    }

    @Override
    public void cancelarAgendamento(Long agendamentoId, Long usuarioId, String motivo) {
        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado."));

        boolean ehCliente = agendamento.getCliente().getId().equals(usuarioId);
        boolean ehProfissional = agendamento.getProfissional().getUsuario().getId().equals(usuarioId);

        if(!ehCliente && !ehProfissional) {
            throw new IllegalArgumentException("Usuário não tem permissão para cancelar este agendamento.");
        }

        agendamento.setStatus(ehCliente ? AgendamentoStatus.CANCELADO_CLIENTE : AgendamentoStatus.CANCELADO_PROFISSIONAL);

        agendamento.setMotivoCancelamento(motivo);
        agendamento.setCanceladoEm(LocalDateTime.now());
        agendamento.setAtualizadoEm(LocalDateTime.now());

        agendamentoRepository.save(agendamento);
    }

    @Override
    public Agendamento confirmarAgendamento(Long agendamentoId, Long profissionalId) {
        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado."));

        if(!agendamento.getProfissional().getId().equals(profissionalId)) {
            throw new IllegalArgumentException("Profissional não tem permissão para confirmar este agendamento.");
        }

        agendamento.setStatus(AgendamentoStatus.CONFIRMADO);
        agendamento.setAtualizadoEm(LocalDateTime.now());

        return agendamentoRepository.save(agendamento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Agendamento> listarAgendamentosProfissionalNoDia(Long profissionalId, LocalDate dia) {
        LocalDateTime inicio = dia.atStartOfDay();
        LocalDateTime fim = dia.atTime(LocalTime.MAX);

        return agendamentoRepository.findByProfissionalIdAndDataHoraInicioBetweenOrderByDataHoraInicioAsc(
                profissionalId, inicio, fim
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Agendamento> listarAgendamentosClienteFuturos(Long clienteId) {
        LocalDateTime agora = LocalDateTime.now();
        return agendamentoRepository.findByClienteIdAndDataHoraInicioGreaterThanEqualOrderByDataHoraInicioAsc(
                clienteId, agora
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocalDateTime> listarHorariosDisponiveis(Long profissionalId, Long servicoId, LocalDate dia) {
        Profissional profissional = profissionalRepository.findById(profissionalId)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado."));

        Servico servico = servicoRepository.findById(servicoId)
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado."));

        DayOfWeek diaSemana = dia.getDayOfWeek();

        var disponibilidades = disponibilidadeRepository
                .findByProfissionalIdAndDiaSemanaOrderByHoraInicioAsc(profissional.getId(), diaSemana);

        if(disponibilidades.isEmpty()) {
            return List.of();
        }

        List<LocalDateTime> horariosLivres = new ArrayList<>();
        int duracao = servico.getDuracaoMinutos();

        for(var disp : disponibilidades) {
            LocalDateTime slotInicio = dia.atTime(disp.getHoraInicio());
            LocalDateTime limite = dia.atTime(disp.getHoraFim());

            while (!slotInicio.plusMinutes(duracao).isAfter(limite)) {
                LocalDateTime slotFim = slotInicio.plusMinutes(duracao);

                boolean conflito = agendamentoRepository.existsConflitoHorario(
                        profissionalId, slotInicio, slotFim
                );

                if(!conflito) {
                    horariosLivres.add(slotInicio);
                }

                slotInicio = slotInicio.plusMinutes(duracao);
            }
        }

        return horariosLivres;
    }


//   ----------------- METODO PRIVADO PARA VALIDAR DISPONIVILIDADE -------------------

    private void validarHorarioDentroDaDisponibilidade(Profissional profissional,
                                                       LocalDateTime inicio,
                                                       LocalDateTime fim) {
        DayOfWeek diaSemana = inicio.getDayOfWeek();

        var disponibilidades = disponibilidadeRepository
                .findByProfissionalIdAndDiaSemanaOrderByHoraInicioAsc(profissional.getId(), diaSemana);

        if(disponibilidades.isEmpty()) {
            throw new IllegalArgumentException("Profissional não possui disponibilidade cadastrada para esse dia!");
        }

        boolean dentroDeAlgumaJanela = disponibilidades.stream().anyMatch(disp -> {
            LocalTime iniDisp = disp.getHoraInicio();
            LocalTime fimDisp = disp.getHoraFim();

            LocalTime iniAg = inicio.toLocalTime();
            LocalTime fimAg = fim.toLocalTime();

            return !iniAg.isBefore(iniDisp) && !fimAg.isAfter(fimDisp);
        });

        if(!dentroDeAlgumaJanela) {
            throw new IllegalArgumentException("Horário escolhido fora da disponibilidade do profissional.");
        }
    }
}
