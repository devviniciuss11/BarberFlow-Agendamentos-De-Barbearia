package com.devflows.barberflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload para cadastro ou atualizacao de barbeiro.")
public record BarbeiroRequestDTO(

        @Schema(description = "ID do barbeiro (opcional no payload).", example = "1")
        Long id,

        @Schema(description = "Nome do barbeiro.", example = "Matheus Guilherme")
        @NotBlank(message = "O nome e obrigatorio!")
        @Size(max = 100, message = "O nome deve ter no maximo 100 caracteres!")
        String nome,

        @Schema(description = "Especialidade principal.", example = "Fade")
        @NotBlank(message = "A especialidade e obrigatoria!")
        @Size(max = 100, message = "A especialidade deve ter no maximo 100 caracteres!")
        String especialidade,

        @Schema(description = "Telefone com apenas numeros.", example = "11999990001")
        @NotBlank(message = "O telefone e obrigatorio!")
        @Pattern(regexp = "\\d+", message = "Telefone deve conter apenas numeros.")
        @Size(max = 20, message = "O telefone deve ter no maximo 15 caracteres!")
        String telefone,

        @Schema(description = "Senha do barbeiro.", example = "senhaB")
        @NotBlank(message = "Senha obrigatoria!")
        String senha,

        @Schema(description = "CPF do barbeiro.", example = "12345678900")
        @NotBlank(message = "CPF e obrigatorio!")
        @Size(max = 14, message = "O CPF deve ter no maximo 14 caracteres!")
        String cpf

) {}
