package com.devflows.barberflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "Resposta com dados de horario disponivel.")
public record HorarioResponseDTO (
        @Schema(description = "ID do horario.", example = "1")
        Long id,
        @Schema(description = "Nome do barbeiro.", example = "Barbeiro Postman")
        String barbeiro,
        @Schema(description = "Data do horario.", example = "2026-12-31")
        LocalDate data,
        @Schema(description = "Hora do horario.", example = "08:00:00")
        LocalTime hora,
        @Schema(description = "Indica se o horario esta disponivel.", example = "true")
        Boolean disponivel
) {}
