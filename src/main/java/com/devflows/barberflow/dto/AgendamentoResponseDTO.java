package com.devflows.barberflow.dto;
import com.devflows.barberflow.entity.Agendamento.StatusAgendamento;
import java.time.LocalDate;
import java.time.LocalTime;

public record AgendamentoResponseDTO(
        Long id,
        Long clienteId,
        String clienteNome,
        String clienteTelefone,
        LocalDate data,
        LocalTime horario,
        String servico,
        StatusAgendamento status
) {}
