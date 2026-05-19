package com.devflows.barberflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "Payload para criacao de agendamento.")
public record AgendamentoRequestDTO(

        @Schema(description = "Nome do cliente.", example = "Cliente Postman")
        @NotBlank(message = "O nome do cliente e obrigatorio.")
        String nomeCliente,

        @Schema(description = "Telefone do cliente com apenas numeros.", example = "11988880001")
        @NotBlank(message = "O telefone do cliente e obrigatorio.")
        @Pattern(regexp = "\\d+", message = "Telefone do cliente deve conter apenas numeros.")
        String telefoneCliente,

        @Schema(description = "Telefone do barbeiro com apenas numeros.", example = "11999990001")
        @NotBlank(message = "O telefone do barbeiro e obrigatorio.")
        @Pattern(regexp = "\\d+", message = "Telefone do barbeiro deve conter apenas numeros.")
        String telefoneBarbeiro,

        @Schema(description = "Data do agendamento.", example = "2026-12-31")
        @NotNull(message = "A data e obrigatoria.")
        @FutureOrPresent(message = "A data nao pode ser no passado.")
        LocalDate data,

        @Schema(description = "Horario do agendamento.", example = "09:00:00")
        @NotNull(message = "A hora e obrigatoria.")
        LocalTime hora
) {}
