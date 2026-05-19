package com.devflows.barberflow.repository;

import com.devflows.barberflow.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    @Query(value = """
            SELECT a.id, a.cliente_id, a.barbeiro_id, a.data, a.horario, a.status, a.cancelado
            FROM agendamento a
            INNER JOIN barbeiro b ON b.id = a.barbeiro_id
            WHERE b.telefone = :telefoneBarbeiro
            ORDER BY a.data ASC, a.horario ASC
            """, nativeQuery = true)
    List<Agendamento> findByBarbeiroTelefoneOrderByDataAscHorarioAsc(@Param("telefoneBarbeiro") String telefoneBarbeiro);

    @Query(value = """
            SELECT a.id, a.cliente_id, a.barbeiro_id, a.data, a.horario, a.status, a.cancelado
            FROM agendamento a
            INNER JOIN barbeiro b ON b.id = a.barbeiro_id
            WHERE b.telefone = :telefoneBarbeiro
              AND a.cancelado = FALSE
            ORDER BY a.data ASC, a.horario ASC
            """, nativeQuery = true)
    List<Agendamento> findByBarbeiroTelefoneAndCanceladoFalseOrderByDataAscHorarioAsc(@Param("telefoneBarbeiro") String telefoneBarbeiro);

    @Query(value = """
            SELECT a.id, a.cliente_id, a.barbeiro_id, a.data, a.horario, a.status, a.cancelado
            FROM agendamento a
            INNER JOIN barbeiro b ON b.id = a.barbeiro_id
            WHERE b.telefone = :telefoneBarbeiro
              AND a.cancelado = FALSE
              AND a.status = FALSE
            ORDER BY a.data ASC, a.horario ASC
            """, nativeQuery = true)
    List<Agendamento> findByBarbeiroTelefoneAndCanceladoFalseAndStatusFalseOrderByDataAscHorarioAsc(@Param("telefoneBarbeiro") String telefoneBarbeiro);

    boolean existsByBarbeiroIdAndDataAndHorarioAndCanceladoFalse(Long barbeiroId, LocalDate data, LocalTime horario);

    boolean existsByBarbeiroIdAndDataAndHorarioAndCanceladoFalseAndIdNot(
            Long barbeiroId, LocalDate data, LocalTime horario, Long id
    );
}
