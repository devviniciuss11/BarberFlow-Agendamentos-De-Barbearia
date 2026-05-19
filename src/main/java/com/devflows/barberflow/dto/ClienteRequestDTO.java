package com.devflows.barberflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Payload para cadastro ou atualizacao de cliente.")
public record ClienteRequestDTO(
        @Schema(description = "Nome completo do cliente.", example = "Vinicius Silva")
        @NotBlank(message = "Nome do cliente e obrigatorio.")
        String nome,

        @Schema(description = "Telefone do cliente com apenas numeros.", example = "11988880001")
        @NotBlank(message = "Telefone e obrigatorio.")
        @Pattern(regexp = "\\d+", message = "Telefone deve conter apenas numeros.")
        String telefone,

        @Schema(description = "Senha de acesso do cliente.", example = "senhaC")
        @NotBlank(message = "Senha e obrigatoria.")
        String senha
) {}
