package com.devflows.barberflow.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record HorarioResponseDTO (Long id, String barbeiro, LocalDate data, LocalTime hora, Boolean disponivel){}
