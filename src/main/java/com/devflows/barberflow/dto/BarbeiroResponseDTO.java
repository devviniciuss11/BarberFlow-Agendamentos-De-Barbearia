package com.devflows.barberflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
@Schema(description = "Resposta com dados de barbeiro.")
public record BarbeiroResponseDTO(

        @Schema(description = "ID do barbeiro.", example = "1")
        Long id,
        @Schema(description = "Especialidade do barbeiro.", example = "Fade")
        String especialidade,
        @Schema(description = "Nome do barbeiro.", example = "Barbeiro Postman")
        String nome,
        @Schema(description = "Telefone do barbeiro.", example = "11999990001")
        String telefone,
        @Schema(description = "CPF do barbeiro.", example = "12345678900")
        String cpf,
        @Schema(description = "Status de atividade do barbeiro.", example = "true")
        Boolean ativo
){}

