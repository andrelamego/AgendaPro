package com.lamego.agendapro.service.implementation;

import com.lamego.agendapro.domain.enums.UserRole;
import com.lamego.agendapro.domain.model.Profissional;
import com.lamego.agendapro.domain.model.User;
import com.lamego.agendapro.dto.command.AtualizarProfissionalCommand;
import com.lamego.agendapro.dto.command.CriarProfissionalCommand;
import com.lamego.agendapro.repository.ProfissionalRepository;
import com.lamego.agendapro.repository.UserRepository;
import com.lamego.agendapro.service.interfaces.ProfissionalService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfissionalServiceImpl implements ProfissionalService {

    private final ProfissionalRepository profissionalRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Profissional criarProfissional(CriarProfissionalCommand command) {
        if(userRepository.existsByEmail(command.email())) {
            throw new IllegalArgumentException("Já existe um usuário com esse email.");
        }

        User usuario = User.builder()
                .nome(command.nome())
                .email(command.email())
                .senhaHash(passwordEncoder.encode(command.senha()))
                .telefone(command.telefone())
                .role(UserRole.PROFISSIONAL)
                .criadoEm(LocalDateTime.now())
                .ativo(true)
                .build();

        usuario = userRepository.save(usuario);

        Profissional profissional = Profissional.builder()
                .usuario(usuario)
                .bio(command.bio())
                .aceitaNovosClientes(command.aceitaNovosClientes())
                .ativo(true)
                .build();

        return profissionalRepository.save(profissional);
    }

    @Override
    public Profissional atualizarPerfil(Long profissionalId, AtualizarProfissionalCommand command) {
        Profissional profissional = buscarPorId(profissionalId);
        User usuario = profissional.getUsuario();

        if (command.nome() != null && !command.nome().isBlank()) {
            usuario.setNome(command.nome());
        }
        if (command.telefone() != null) {
            usuario.setTelefone(command.telefone());
        }
        if (command.bio() != null) {
            profissional.setBio(command.bio());
        }
        if (command.aceitaNovosClientes() != null) {
            profissional.setAceitaNovosClientes(command.aceitaNovosClientes());
        }
        if (command.ativo() != null) {
            profissional.setAtivo(command.ativo());
        }

        userRepository.save(usuario);
        return profissionalRepository.save(profissional);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Profissional> listarAtivos(boolean apenasAceitandoNovos) {
        if(apenasAceitandoNovos) {
            return profissionalRepository.findByAtivoTrueAndAceitaNovosClientesTrue();
        } else {
            return profissionalRepository.findByAtivoTrue();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Profissional buscarPorId(Long id) {
        return profissionalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado."));
    }
}
