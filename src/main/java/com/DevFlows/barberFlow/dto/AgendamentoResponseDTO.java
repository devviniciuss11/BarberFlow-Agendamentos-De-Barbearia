package com.DevFlows.barberFlow.dto;
import com.DevFlows.barberFlow.Entity.Agendamento.StatusAgendamento;
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