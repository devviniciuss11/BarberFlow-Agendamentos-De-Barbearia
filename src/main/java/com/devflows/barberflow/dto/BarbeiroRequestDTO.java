package com.devflows.barberflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.query.JSqlParserQueryEnhancer;

public record BarbeiroRequestDTO (

        Long id,

        @NotBlank(message = "O Nome é Obrigatório!")
        @Size(max = 100, message = "O Nome deve ter no Max: 100 Caracteres!")
        String nome,

        @NotBlank(message = "A especialidade é Obrigatória!")
        @Size(max = 100, message = "A especialidade deve ter no Max: 100 Caracteres!")
        String especialidade,

        @NotBlank(message = "O telefone é Obrigatório!")
        @Size(max = 20, message = "O telefone deve ter no Max: 15 Caracteres!")
        String telefone,

        @NotBlank(message = "Senha Obrigatória!")
        String senha,

        @NotBlank(message = "CPF é Obrigatório!")
        @Size(max = 14, message = "O CPF deve ter no Max: 14 Caracteres!")
        String cpf

        ){}




