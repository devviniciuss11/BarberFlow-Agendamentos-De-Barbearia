package com.devflows.barberflow.repositorys;

import com.devflows.barberflow.entity.Agendamento;
import com.devflows.barberflow.entity.Agendamento.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    List<Agendamento> findByClienteIdOrderByDataAscHorarioAsc(Long clienteId);

    List<Agendamento> findByDataOrderByHorarioAsc(LocalDate data);

    boolean existsByDataAndHorarioAndStatusNot(LocalDate data, LocalTime horario, StatusAgendamento status);

    @Query("""
            SELECT a FROM Agendamento a
            WHERE a.data = :data
              AND a.horario = :horario
              AND a.status <> :status
              AND a.id <> :id
            """)
    boolean existsByDataAndHorarioAndStatusNotAndIdNot(
            @Param("data") LocalDate data,
            @Param("horario") LocalTime horario,
            @Param("status") StatusAgendamento status,
            @Param("id") Long id
    );

    @Query("""
            SELECT a FROM Agendamento a
            WHERE (:clienteId IS NULL OR a.cliente.id = :clienteId)
              AND (:data IS NULL OR a.data = :data)
              AND (:status IS NULL OR a.status = :status)
            ORDER BY a.data ASC, a.horario ASC
            """)
    List<Agendamento> buscarComFiltros(
            @Param("clienteId") Long clienteId,
            @Param("data") LocalDate data,
            @Param("status") StatusAgendamento status
    );
}
