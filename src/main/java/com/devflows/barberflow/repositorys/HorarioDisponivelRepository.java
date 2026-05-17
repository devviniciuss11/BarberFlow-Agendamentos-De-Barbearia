package com.devflows.barberflow.repositorys;

import com.devflows.barberflow.entity.HorarioDisponivel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HorarioDisponivelRepository extends JpaRepository<HorarioDisponivel, Long> {
    List<HorarioDisponivel> findByBarbeiroIdAndDataAndDisponivelTrueOrderByHoraAsc(Long barbeiroId, LocalDate data);

    boolean existsByBarbeiroIdAndDataAndHora(Long barbeiroId, LocalDate data, LocalTime hora);

    Optional<HorarioDisponivel> findByBarbeiroIdAndDataAndHoraAndDisponivelTrue(Long barbeiroId, LocalDate data, LocalTime hora);
}
