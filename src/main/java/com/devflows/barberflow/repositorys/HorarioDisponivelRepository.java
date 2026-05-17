package com.devflows.barberflow.repositorys;

import com.devflows.barberflow.entity.HorarioDisponivel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HorarioDisponivelRepository extends JpaRepository<HorarioDisponivel, Integer> {
    @Query (value = """
            SELECT * FROM horariosDisponiveis
            WHERE barbeiro_id = :barbeiroId
            AND data = :data
            AND disponivel = true
            ORDER BY hora
            """, nativeQuery = true)
    List<HorarioDisponivel> buscarHorariosDisponiveis(
            @Param("barbeiroId") Long barbeiroId,
            @Param("data")LocalDate data
    );

    @Query(value = """
            SELECT * FROM horariosDisponiveis
            WHERE barbeiro_id = :barbeiroId
            AND data = :data
            AND hora = :hora
            """, nativeQuery = true)
    Optional<HorarioDisponivel> bucarHorarioLivre(
            @Param("barbeiroid") Long barbeiroID,
            @Param("data")LocalDate data,
            @Param("hora")LocalTime hora
    );

    @Query(value = """
            SELECT * FROM horariosDisponiveis
            WHERE barbeiro_id = :barbeiroId
            AND data = :data
            AND hora = :hora
            """, nativeQuery = true)
    Optional<HorarioDisponivel> buscarHorarioPorBarbeiroDataHora(
            @Param("barbeiroId") Long barbeiroId,
            @Param("data") LocalDate data,
            @Param("hora") LocalTime hora
    );
}
