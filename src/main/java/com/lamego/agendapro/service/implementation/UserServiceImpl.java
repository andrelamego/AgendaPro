package com.lamego.agendapro.service.implementation;

import com.lamego.agendapro.domain.enums.UserRole;
import com.lamego.agendapro.domain.model.User;
import com.lamego.agendapro.dto.user.command.RegistrarClienteCommand;
import com.lamego.agendapro.repository.UserRepository;
import com.lamego.agendapro.service.interfaces.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registrarCliente(RegistrarClienteCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            throw new IllegalArgumentException("Já existe usuário com este e-mail.");
        }

        User user = User.builder()
                .nome(command.nome())
                .email(command.email())
                .senhaHash(passwordEncoder.encode(command.senha()))
                .telefone(command.telefone())
                .role(UserRole.CLIENTE)
                .criadoEm(LocalDateTime.now())
                .ativo(true)
                .build();

        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> buscarPorEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public User buscarPorId(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));
    }

    @Override
    public void ativarUsuario(Long id) {
        User user = buscarPorId(id);
        user.setAtivo(true);
        userRepository.save(user);
    }

    @Override
    public void desativarUsuario(Long id) {
        User user = buscarPorId(id);
        user.setAtivo(false);
        userRepository.save(user);
    }
}
