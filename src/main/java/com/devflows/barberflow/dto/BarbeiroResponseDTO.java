package com.devflows.barberflow.dto;

public record BarbeiroResponseDTO(

        Long id,
        String especialidade,
        String nome,
        String telefone,
        String cpf,
        Boolean ativo
){}

