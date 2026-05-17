package com.devflows.barberflow.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record AgendamentoResponseDTO(
        Long id,
        String clienteNome,
        String clienteTelefone,
        String barbeiroNome,
        String barbeiroTelefone,
        LocalDate data,
        LocalTime hora,
        Boolean status,
        Boolean cancelado
) {}
