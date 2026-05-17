package com.devflows.barberflow.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "horariosDisponiveis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class HorarioDisponivel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private LocalTime hora;

    @Column(nullable = false)
    private Boolean disponivel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barbeiro_id", nullable = false)
    private com.devflows.barberflow.entity.Barbeiro barbeiro;


}
