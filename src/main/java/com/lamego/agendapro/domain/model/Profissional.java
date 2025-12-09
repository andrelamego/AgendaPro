package com.lamego.agendapro.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "profissionais")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Profissional {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, foreignKey = @ForeignKey(name = "fk_profissional_usuario"))
    private User usuario;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "aceita_novos_clientes", nullable = false)
    private Boolean aceitaNovosClientes;

    @Column(nullable = false)
    private Boolean ativo;
}
