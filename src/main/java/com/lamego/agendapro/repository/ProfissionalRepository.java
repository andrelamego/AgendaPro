package com.lamego.agendapro.repository;

import com.lamego.agendapro.domain.model.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {

    // Encontrar profissional pelo usuario
    Optional<Profissional> findByUsuarioId(Long usuarioId);

    // Listar profissionais ativos
    List<Profissional> findByAtivoTrue();

    // Listar ativos que aceitam novos clientes
    List<Profissional> findByAtivoTrueAndAceitaNovosClientesTrue();

    // Filtro por nome via join implicito em User.nome
    List<Profissional> findByUsuario_NomeContainingIgnoreCaseAndAtivoTrue(String nome);
}
