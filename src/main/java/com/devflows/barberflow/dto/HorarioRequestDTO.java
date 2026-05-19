package com.devflows.barberflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "Payload para cadastro de horario disponivel.")
public record HorarioRequestDTO(
        @Schema(description = "ID do barbeiro dono do horario.", example = "1")
        Long barbeiroid,
        @Schema(description = "Data do horario.", example = "2026-12-31")
        LocalDate data,
        @Schema(description = "Hora do horario.", example = "08:00:00")
        LocalTime hora,
        @Schema(description = "Disponibilidade inicial do horario.", example = "true")
        Boolean disponivel
) {}
