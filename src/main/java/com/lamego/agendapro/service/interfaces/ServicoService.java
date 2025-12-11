package com.lamego.agendapro.service.interfaces;

import com.lamego.agendapro.domain.model.Servico;
import com.lamego.agendapro.dto.command.AtualizarServicoCommand;
import com.lamego.agendapro.dto.command.CriarServicoCommand;

import java.util.List;

public interface ServicoService {

    Servico criarServico(CriarServicoCommand command);

    Servico atualizarServico(Long id, AtualizarServicoCommand command);

    List<Servico> listarServicosAtivos(String filtroNome);

    void inativarServico(Long id);

    Servico buscarPorId(Long id);
}
