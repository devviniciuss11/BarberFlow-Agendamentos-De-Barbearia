package com.devflows.barberflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ClienteRequestDTO(
        @NotBlank(message = "Nome do cliente e obrigatorio.")
        String nome,

        @NotBlank(message = "Telefone e obrigatorio.")
        @Pattern(regexp = "\\d+", message = "Telefone deve conter apenas numeros.")
        String telefone,

        @NotBlank(message = "Senha e obrigatoria.")
        String senha
) {}
