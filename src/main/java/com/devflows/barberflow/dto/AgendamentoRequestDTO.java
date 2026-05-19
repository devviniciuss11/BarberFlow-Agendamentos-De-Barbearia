package com.devflows.barberflow.dto;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.time.LocalTime;

public record AgendamentoRequestDTO(

        @NotBlank(message = "O nome do cliente e obrigatorio.")
        String nomeCliente,

        @NotBlank(message = "O telefone do cliente e obrigatorio.")
        @Pattern(regexp = "\\d+", message = "Telefone do cliente deve conter apenas numeros.")
        String telefoneCliente,

        @NotBlank(message = "O telefone do barbeiro e obrigatorio.")
        @Pattern(regexp = "\\d+", message = "Telefone do barbeiro deve conter apenas numeros.")
        String telefoneBarbeiro,

        @NotNull(message = "A data e obrigatoria.")
        @FutureOrPresent(message = "A data nao pode ser no passado.")
        LocalDate data,

        @NotNull(message = "A hora e obrigatoria.")
        LocalTime hora
) {}
