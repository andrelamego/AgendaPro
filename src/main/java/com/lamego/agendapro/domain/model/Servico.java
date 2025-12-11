package com.lamego.agendapro.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "servicos",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_servicos_profissional_nome",
                        columnNames = {"profissional_id", "nome"}
                )
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // dono do serviço
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "profissional_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_servico_profissional")
    )
    private Profissional profissional;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @Column(name = "duracao_minutos", nullable = false)
    private Integer duracaoMinutos;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(nullable = false)
    private Boolean ativo;
}
