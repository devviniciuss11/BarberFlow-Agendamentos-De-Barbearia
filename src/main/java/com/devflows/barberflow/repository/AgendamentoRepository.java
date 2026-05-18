package com.devflows.barberflow.repository;

import com.devflows.barberflow.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    List<Agendamento> findByBarbeiroTelefoneOrderByDataAscHorarioAsc(String telefoneBarbeiro);

    List<Agendamento> findByBarbeiroTelefoneAndCanceladoFalseOrderByDataAscHorarioAsc(String telefoneBarbeiro);

    List<Agendamento> findByBarbeiroTelefoneAndCanceladoFalseAndStatusFalseOrderByDataAscHorarioAsc(String telefoneBarbeiro);

    boolean existsByBarbeiroIdAndDataAndHorarioAndCanceladoFalse(Long barbeiroId, LocalDate data, LocalTime horario);

    boolean existsByBarbeiroIdAndDataAndHorarioAndCanceladoFalseAndIdNot(
            Long barbeiroId, LocalDate data, LocalTime horario, Long id
    );
}
