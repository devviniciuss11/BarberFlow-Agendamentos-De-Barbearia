package com.devflows.barberflow.dto;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record AgendamentoRequestDTO(

        @NotNull(message = "O ID do cliente é obrigatório.")
        Long clienteId,

        @NotNull(message = "A data é obrigatória.")
        @FutureOrPresent(message = "A data não pode ser no passado.")
        LocalDate data,

        @NotNull(message = "O horário é obrigatório.")
        LocalTime horario,

        @NotBlank(message = "O serviço é obrigatório.")
        String servico
) {}


