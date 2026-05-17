package com.devflows.barberflow.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record HorarioRequestDTO (Long barbeiroid, LocalDate data, LocalTime hora, Boolean disponivel){}
