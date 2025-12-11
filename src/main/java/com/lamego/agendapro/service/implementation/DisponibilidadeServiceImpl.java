package com.lamego.agendapro.service.implementation;

import com.lamego.agendapro.domain.model.Disponibilidade;
import com.lamego.agendapro.domain.model.Profissional;
import com.lamego.agendapro.dto.disponibilidade.command.AdicionarDisponibilidadeCommand;
import com.lamego.agendapro.repository.DisponibilidadeRepository;
import com.lamego.agendapro.repository.ProfissionalRepository;
import com.lamego.agendapro.service.interfaces.DisponibilidadeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DisponibilidadeServiceImpl implements DisponibilidadeService {

    private final DisponibilidadeRepository disponibilidadeRepository;
    private final ProfissionalRepository profissionalRepository;
    private final DisponibilidadeService disponibilidadeService;

    @Override
    public Disponibilidade adicionarDisponibilidade(AdicionarDisponibilidadeCommand command) {
        Profissional profissional = profissionalRepository.findById(command.profissionalId())
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado."));

        if(command.horaFim().isBefore(command.horaInicio()) || command.horaFim().equals(command.horaInicio())) {
            throw new IllegalArgumentException("Horário final deve ser maior que o inicial.");
        }

        boolean existe = disponibilidadeRepository.existsByProfissionalIdAndDiaSemanaAndHoraInicioAndHoraFim(
                profissional.getId(),
                command.diaSemana(),
                command.horaInicio(),
                command.horaFim()
        );

        if(existe) {
            throw new IllegalArgumentException("Já existe uma disponibilidade cadastrada com esses parâmetros");
        }

        Disponibilidade disponibilidade = Disponibilidade.builder()
                .profissional(profissional)
                .diaSemana(command.diaSemana())
                .horaInicio(command.horaInicio())
                .horaFim(command.horaFim())
                .build();

        return disponibilidadeRepository.save(disponibilidade);
    }

    @Override
    public void removerDisponibilidade(Long disponibilidadeId, Long profissionalId) {
        Disponibilidade disponibilidade = disponibilidadeRepository.findById(disponibilidadeId)
                .orElseThrow(() -> new EntityNotFoundException("Disponibilidade não encontrada."));

        if(!disponibilidade.getProfissional().getId().equals(profissionalId)) {
            throw new IllegalArgumentException("Profissional não tem permissão para remover esta disponibilidade.");
        }

        disponibilidadeRepository.delete(disponibilidade);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Disponibilidade> listarPorProfissional(Long profissionalId) {
        return disponibilidadeRepository.findByProfissionalIdOrderByDiaSemanaAscHoraInicioAsc(profissionalId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Disponibilidade> listarPorProfissionalEDia(Long profissionalId, DayOfWeek diaSemana) {
        return disponibilidadeRepository.findByProfissionalIdAndDiaSemanaOrderByHoraInicioAsc(
                profissionalId,
                diaSemana
        );
    }
}
