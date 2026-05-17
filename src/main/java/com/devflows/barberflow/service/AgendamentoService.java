package com.devflows.barberflow.service;

import com.devflows.barberflow.entity.Agendamento;
import com.devflows.barberflow.entity.Agendamento.StatusAgendamento;
import com.devflows.barberflow.entity.Cliente;
import com.devflows.barberflow.repositorys.AgendamentoRepository;
import com.devflows.barberflow.repositorys.ClienteRepository;
import com.devflows.barberflow.dto.AgendamentoRequestDTO;
import com.devflows.barberflow.dto.AgendamentoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final ClienteRepository clienteRepository;

    public AgendamentoResponseDTO agendar(AgendamentoRequestDTO dto) {
        Cliente cliente = buscarClienteOuLancarErro(dto.clienteId());

        boolean horarioOcupado = agendamentoRepository
                .existsByDataAndHorarioAndStatusNot(dto.data(), dto.horario(), StatusAgendamento.CANCELADO);

        if (horarioOcupado) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Já existe um agendamento para esse dia e horário.");
        }

        Agendamento agendamento = Agendamento.builder()
                .cliente(cliente)
                .data(dto.data())
                .horario(dto.horario())
                .servico(dto.servico())
                .status(StatusAgendamento.PENDENTE)
                .build();

        return toResponseDTO(agendamentoRepository.save(agendamento));
    }

    public AgendamentoResponseDTO buscarPorId(Long id) {
        return toResponseDTO(buscarAgendamentoOuLancarErro(id));
    }

    public List<AgendamentoResponseDTO> listarComFiltros(Long clienteId, LocalDate data, StatusAgendamento status) {
        return agendamentoRepository
                .buscarComFiltros(clienteId, data, status)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public AgendamentoResponseDTO atualizar(Long id, AgendamentoRequestDTO dto) {
        Agendamento agendamento = buscarAgendamentoOuLancarErro(id);
        Cliente cliente = buscarClienteOuLancarErro(dto.clienteId());

        boolean horarioOcupado = agendamentoRepository
                .existsByDataAndHorarioAndStatusNotAndIdNot(
                        dto.data(), dto.horario(), StatusAgendamento.CANCELADO, id);

        if (horarioOcupado) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Já existe um agendamento para esse dia e horário.");
        }

        agendamento.setCliente(cliente);
        agendamento.setData(dto.data());
        agendamento.setHorario(dto.horario());
        agendamento.setServico(dto.servico());

        return toResponseDTO(agendamentoRepository.save(agendamento));
    }

    public AgendamentoResponseDTO atualizarStatus(Long id, StatusAgendamento novoStatus) {
        Agendamento agendamento = buscarAgendamentoOuLancarErro(id);
        agendamento.setStatus(novoStatus);
        return toResponseDTO(agendamentoRepository.save(agendamento));
    }

    public void excluir(Long id) {
        buscarAgendamentoOuLancarErro(id);
        agendamentoRepository.deleteById(id);
    }

    private Agendamento buscarAgendamentoOuLancarErro(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Agendamento não encontrado com id: " + id));
    }

    private Cliente buscarClienteOuLancarErro(Long clienteId) {
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente não encontrado com id: " + clienteId));
    }

    private AgendamentoResponseDTO toResponseDTO(Agendamento a) {
        return new AgendamentoResponseDTO(
                a.getId(),
                a.getCliente().getId(),
                a.getCliente().getNome(),
                a.getCliente().getTelefone(),
                a.getData(),
                a.getHorario(),
                a.getServico(),
                a.getStatus()
        );
    }
}
