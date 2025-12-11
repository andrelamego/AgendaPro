package com.lamego.agendapro.service.interfaces;

import com.lamego.agendapro.domain.model.Servico;
import com.lamego.agendapro.dto.servico.command.AtualizarServicoCommand;
import com.lamego.agendapro.dto.servico.command.CriarServicoCommand;

import java.util.List;

public interface ServicoService {

    Servico criarServico(Long profissionalId, CriarServicoCommand command);

    Servico atualizarServico(Long profissionalId, Long servicoId, AtualizarServicoCommand command);

    List<Servico> listarServicosDoProfissional(Long profissionalId, String filtroNome);

    void inativarServico(Long profissionalId, Long servicoId);

    Servico buscarPorIdEProfissional(Long profissionalId, Long servicoId);

    // para os clientes verem os serviços de um profissional
    List<Servico> listarServicosDoProfissionalPublic(Long profissionalId);
}
