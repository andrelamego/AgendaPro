package com.lamego.agendapro.service.interfaces;

import com.lamego.agendapro.domain.model.User;
import com.lamego.agendapro.dto.user.command.RegistrarClienteCommand;

import java.util.Optional;

public interface UserService {

    User registrarCliente(RegistrarClienteCommand command);

    Optional<User> buscarPorEmail(String email);

    User buscarPorId(Long id);

    void ativarUsuario(Long id);

    void desativarUsuario(Long id);
}
