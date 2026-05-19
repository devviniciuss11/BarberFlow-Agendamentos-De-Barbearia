package com.devflows.barberflow.repository;
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
public interface HorarioDisponivelRepository extends JpaRepository<HorarioDisponivel, Long> {
    @Query(value = """
            SELECT h.id, h.data, h.hora, h.disponivel, h.barbeiro_id
            FROM horarios_disponiveis h
            WHERE h.barbeiro_id = :barbeiroId
              AND h.data = :data
              AND h.disponivel = TRUE
            ORDER BY h.hora ASC
            """, nativeQuery = true)
    List<HorarioDisponivel> findByBarbeiroIdAndDataAndDisponivelTrueOrderByHoraAsc(
            @Param("barbeiroId") Long barbeiroId,
            @Param("data") LocalDate data
    );

    boolean existsByBarbeiroIdAndDataAndHora(Long barbeiroId, LocalDate data, LocalTime hora);

    Optional<HorarioDisponivel> findByBarbeiroIdAndDataAndHoraAndDisponivelTrue(Long barbeiroId, LocalDate data, LocalTime hora);

    Optional<HorarioDisponivel> findByBarbeiroIdAndDataAndHoraAndDisponivelFalse(
            Long barbeiroId,
            LocalDate data,
            LocalTime hora
    );
}
