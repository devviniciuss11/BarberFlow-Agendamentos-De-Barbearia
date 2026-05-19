package com.devflows.barberflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "Resposta com dados de agendamento.")
public record AgendamentoResponseDTO(
        @Schema(description = "ID do agendamento.", example = "1")
        Long id,
        @Schema(description = "Nome do cliente.", example = "Cliente Postman")
        String clienteNome,
        @Schema(description = "Telefone do cliente.", example = "11988880001")
        String clienteTelefone,
        @Schema(description = "Nome do barbeiro.", example = "Barbeiro Postman")
        String barbeiroNome,
        @Schema(description = "Telefone do barbeiro.", example = "11999990001")
        String barbeiroTelefone,
        @Schema(description = "Data do agendamento.", example = "2026-12-31")
        LocalDate data,
        @Schema(description = "Horario do agendamento.", example = "09:00:00")
        LocalTime hora,
        @Schema(description = "Indica se o servico foi concluido.", example = "false")
        Boolean status,
        @Schema(description = "Indica se o agendamento foi cancelado.", example = "false")
        Boolean cancelado
) {}
