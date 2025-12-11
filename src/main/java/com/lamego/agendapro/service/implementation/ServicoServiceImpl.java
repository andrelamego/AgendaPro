package com.lamego.agendapro.service.implementation;

import com.lamego.agendapro.domain.model.Servico;
import com.lamego.agendapro.dto.servico.command.AtualizarServicoCommand;
import com.lamego.agendapro.dto.servico.command.CriarServicoCommand;
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

    @Override
    public Servico criarServico(CriarServicoCommand command) {
        if(servicoRepository.existsByNomeIgnoreCase(command.nome())){
            throw new IllegalArgumentException("Já existe um serviço com este nome.");
        }

        Servico servico = Servico.builder()
                .nome(command.nome())
                .descricao(command.descricao())
                .duracaoMinutos(command.duracaoMinutos())
                .preco(command.preco())
                .ativo(true)
                .build();

        return servicoRepository.save(servico);
    }

    @Override
    public Servico atualizarServico(Long id, AtualizarServicoCommand command) {
        Servico servico = buscarPorId(id);

        if(command.nome() != null && !command.nome().isBlank()) {
            servico.setNome(command.nome());
        }
        if(command.descricao() != null) {
            servico.setDescricao(command.descricao());
        }
        if(command.duracaoMinutos() != null) {
            servico.setDuracaoMinutos(command.duracaoMinutos());
        }
        if(command.preco() != null) {
            servico.setPreco(command.preco());
        }
        if(command.ativo() != null) {
            servico.setAtivo(command.ativo());
        }

        return servicoRepository.save(servico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Servico> listarServicosAtivos(String filtroNome) {
        if(filtroNome == null || filtroNome.isBlank()) {
            return servicoRepository.findByAtivoTrueOrderByNomeAsc();
        }
        return servicoRepository.findByAtivoTrueAndNomeContainingIgnoreCaseOrderByNomeAsc(filtroNome);
    }

    @Override
    public void inativarServico(Long id) {
        Servico servico = buscarPorId(id);
        servico.setAtivo(false);
        servicoRepository.save(servico);
    }

    @Override
    @Transactional(readOnly = true)
    public Servico buscarPorId(Long id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado."));
    }
}
