package com.lamego.agendapro.repository;

import com.lamego.agendapro.domain.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServicoRepository extends JpaRepository<Servico, Long> {

    // serviços ativos de um profissional
    List<Servico> findByProfissionalIdAndAtivoTrueOrderByNomeAsc(Long profissionalId);

    // com filtro de nome
    List<Servico> findByProfissionalIdAndAtivoTrueAndNomeContainingIgnoreCaseOrderByNomeAsc(
            Long profissionalId,
            String nome
    );

    // checar nome duplicado por profissional
    boolean existsByProfissionalIdAndNomeIgnoreCase(Long profissionalId, String nome);

    // buscar garantindo o dono
    Optional<Servico> findByIdAndProfissionalId(Long servicoId, Long profissionalId);
}
