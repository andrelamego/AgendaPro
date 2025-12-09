package com.lamego.agendapro.repository;

import com.lamego.agendapro.domain.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServicoRepository extends JpaRepository<Servico, Long> {

    // Listar servicos ativos para exibir pro cliente
    List<Servico> findByAtivoTrueOrderByNomeAsc();

    // Usar em combos com filtro de texto
    List<Servico> findByAtivoTrueAndNomeContainingIgnoreCaseOrderByNomeAsc(String nome);

    // Garantir que nao tem dois servicos com mesmo nome
    boolean existsByNomeIgnoreCase(String nome);

    // Buscar por nome exato
    Optional<Servico> findByNomeIgnoreCase(String nome);
}
