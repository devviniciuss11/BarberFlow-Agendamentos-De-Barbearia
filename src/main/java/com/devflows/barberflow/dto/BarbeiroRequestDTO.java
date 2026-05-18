package com.devflows.barberflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record BarbeiroRequestDTO(

        Long id,

        @NotBlank(message = "O nome e obrigatorio!")
        @Size(max = 100, message = "O nome deve ter no maximo 100 caracteres!")
        String nome,

        @NotBlank(message = "A especialidade e obrigatoria!")
        @Size(max = 100, message = "A especialidade deve ter no maximo 100 caracteres!")
        String especialidade,

        @NotBlank(message = "O telefone e obrigatorio!")
        @Pattern(regexp = "\\d+", message = "Telefone deve conter apenas numeros.")
        @Size(max = 20, message = "O telefone deve ter no maximo 15 caracteres!")
        String telefone,

        @NotBlank(message = "Senha obrigatoria!")
        String senha,

        @NotBlank(message = "CPF e obrigatorio!")
        @Size(max = 14, message = "O CPF deve ter no maximo 14 caracteres!")
        String cpf

) {}
