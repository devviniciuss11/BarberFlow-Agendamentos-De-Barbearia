package com.devflows.barberflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta com dados de cliente.")
public record ClienteResponseDTO(
        @Schema(description = "ID do cliente.", example = "1")
        Long id,
        @Schema(description = "Nome do cliente.", example = "Cliente Postman")
        String nome,
        @Schema(description = "Telefone do cliente.", example = "11988880001")
        String telefone,
        @Schema(description = "Pontos acumulados em agendamentos concluidos.", example = "3")
        Integer agendamentoPoints
) {
}

