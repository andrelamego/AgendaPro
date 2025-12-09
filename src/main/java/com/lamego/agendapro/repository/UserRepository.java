package com.lamego.agendapro.repository;

import com.lamego.agendapro.domain.enums.UserRole;
import com.lamego.agendapro.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Autenticar usuario
    Optional<User> findByEmail(String email);

    // Validação de cadastro
    boolean existsByEmail(String email);

    // listagem por role
    List<User> findByRole(UserRole role);

    // Buscar apenas users ativos
    List<User> findByRoleAndAtivoTrue(UserRole role);

    // Busca por nome
    List<User> findByNomeContainingIgnoreCase(String nome);
}
