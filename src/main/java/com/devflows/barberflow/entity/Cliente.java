package com.devflows.barberflow.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true, length = 13)
    private String telefone;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private Integer agendamentopoints;


}
