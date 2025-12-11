package com.lamego.agendapro.service.implementation;

import com.lamego.agendapro.domain.model.Profissional;
import com.lamego.agendapro.domain.model.Servico;
import com.lamego.agendapro.dto.servico.command.AtualizarServicoCommand;
import com.lamego.agendapro.dto.servico.command.CriarServicoCommand;
import com.lamego.agendapro.repository.ProfissionalRepository;
import com.lamego.agendapro.repository.ServicoRepository;
import com.lamego.agendapro.service.interfaces.ServicoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ServicoServiceImpl implements ServicoService {

    private final ServicoRepository servicoRepository;
    private final ProfissionalRepository profissionalRepository;

    private Profissional buscarProfissional(Long profissionalId) {
        return profissionalRepository.findById(profissionalId)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));
    }

    @Override
    public Servico criarServico(Long profissionalId, CriarServicoCommand command) {
        Profissional profissional = buscarProfissional(profissionalId);

        if (servicoRepository.existsByProfissionalIdAndNomeIgnoreCase(
                profissionalId, command.nome())) {
            throw new IllegalArgumentException("Já existe um serviço com este nome para este profissional.");
        }

        Servico servico = Servico.builder()
                .profissional(profissional)
                .nome(command.nome())
                .descricao(command.descricao())
                .duracaoMinutos(command.duracaoMinutos())
                .preco(command.preco())
                .ativo(true)
                .build();

        return servicoRepository.save(servico);
    }

    @Override
    public Servico atualizarServico(Long profissionalId, Long servicoId, AtualizarServicoCommand command) {
        Servico servico = buscarPorIdEProfissional(profissionalId, servicoId);

        if (command.nome() != null && !command.nome().isBlank()) {
            servico.setNome(command.nome());
        }
        if (command.descricao() != null) {
            servico.setDescricao(command.descricao());
        }
        if (command.duracaoMinutos() != null) {
            servico.setDuracaoMinutos(command.duracaoMinutos());
        }
        if (command.preco() != null) {
            servico.setPreco(command.preco());
        }
        if (command.ativo() != null) {
            servico.setAtivo(command.ativo());
        }

        return servicoRepository.save(servico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Servico> listarServicosDoProfissional(Long profissionalId, String filtroNome) {
        if (filtroNome == null || filtroNome.isBlank()) {
            return servicoRepository.findByProfissionalIdAndAtivoTrueOrderByNomeAsc(profissionalId);
        }
        return servicoRepository.findByProfissionalIdAndAtivoTrueAndNomeContainingIgnoreCaseOrderByNomeAsc(
                profissionalId, filtroNome
        );
    }

    @Override
    public void inativarServico(Long profissionalId, Long servicoId) {
        Servico servico = buscarPorIdEProfissional(profissionalId, servicoId);
        servico.setAtivo(false);
        servicoRepository.save(servico);
    }

    @Override
    @Transactional(readOnly = true)
    public Servico buscarPorIdEProfissional(Long profissionalId, Long servicoId) {
        return servicoRepository.findByIdAndProfissionalId(servicoId, profissionalId)
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado para este profissional."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Servico> listarServicosDoProfissionalPublic(Long profissionalId) {
        return servicoRepository.findByProfissionalIdAndAtivoTrueOrderByNomeAsc(profissionalId);
    }
}
