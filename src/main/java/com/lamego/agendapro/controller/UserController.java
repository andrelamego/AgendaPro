package com.lamego.agendapro.controller;

import com.lamego.agendapro.domain.model.User;
import com.lamego.agendapro.dto.user.command.RegistrarClienteCommand;
import com.lamego.agendapro.dto.user.request.RegistrarClienteRequest;
import com.lamego.agendapro.dto.user.response.UserResponse;
import com.lamego.agendapro.security.SecurityUtils;
import com.lamego.agendapro.service.interfaces.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register-cliente")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registrarCliente(@RequestBody @Valid RegistrarClienteRequest request) {
        var command = new RegistrarClienteCommand(
                request.nome(),
                request.email(),
                request.senha(),
                request.telefone()
        );

        User user = userService.registrarCliente(command);
        return new UserResponse(
                user.getId(),
                user.getNome(),
                user.getEmail(),
                user.getTelefone()
        );
    }

    @GetMapping("/me")
    public UserResponse me() {
        String email = SecurityUtils.getCurrentUsername();
        User user = userService.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado"));

        return new UserResponse(
                user.getId(),
                user.getNome(),
                user.getEmail(),
                user.getTelefone()
        );
    }
}


