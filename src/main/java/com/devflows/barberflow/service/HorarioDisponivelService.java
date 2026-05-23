package com.devflows.barberflow.service;

import com.devflows.barberflow.dto.HorarioRequestDTO;
import com.devflows.barberflow.dto.HorarioResponseDTO;
import com.devflows.barberflow.entity.Barbeiro;
import com.devflows.barberflow.entity.Cliente;
import com.devflows.barberflow.entity.HorarioDisponivel;
import com.devflows.barberflow.repository.BarbeiroRepository;
import com.devflows.barberflow.repository.ClienteRepository;
import com.devflows.barberflow.repository.HorarioDisponivelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HorarioDisponivelService {

    private final HorarioDisponivelRepository horarioDisponivelRepository;
    private final BarbeiroRepository barbeiroRepository;
    private final ClienteRepository clienteRepository;

    public HorarioResponseDTO cadastrarHorario(HorarioRequestDTO dto) {
        if (dto.barbeiroid() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o ID do barbeiro.");
        }
        if (dto.data() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe a data do horario.");
        }
        if (dto.hora() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe a hora do horario.");
        }

        LocalDateTime dataHoraInformada =
                LocalDateTime.of(dto.data(), dto.hora());

        if (dataHoraInformada.isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Não é permitido cadastrar horários em datas passadas."
            );
        }


        Barbeiro barbeiro = barbeiroRepository.findById(dto.barbeiroid())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Barbeiro nao encontrado."));

        if (horarioDisponivelRepository.existsByBarbeiroIdAndDataAndHora(dto.barbeiroid(), dto.data(), dto.hora())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Este horario ja foi cadastrado para esse barbeiro.");
        }

        HorarioDisponivel horario = HorarioDisponivel.builder()
                .barbeiro(barbeiro)
                .data(dto.data())
                .hora(dto.hora())
                .disponivel(true)
                .build();

        return toResponse(horarioDisponivelRepository.save(horario));
    }

    public List<HorarioResponseDTO> listarDisponiveis(Long barbeiroId, Long clienteId, String data) {
        if (barbeiroId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID do barbeiro e obrigatorio.");
        }
        if (clienteId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID do cliente e obrigatorio.");
        }

        LocalDate dataConvertida = parseData(data);
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente nao encontrado."));

        List<HorarioDisponivel> horarios = horarioDisponivelRepository
                .findByBarbeiroIdAndDataAndDisponivelTrueOrderByHoraAsc(barbeiroId, dataConvertida);

        int limite = calcularLimitePorPontos(cliente.getAgendamentopoints(), horarios.size());

        return horarios.stream()
                .limit(limite)
                .map(this::toResponse)
                .toList();
    }

    private int calcularLimitePorPontos(Integer pontosCliente, int totalHorariosDisponiveis) {
        int pontos = pontosCliente == null ? 0 : pontosCliente;

        if (totalHorariosDisponiveis <= 0) {
            return 0;
        }
        if (pontos >= 10) {
            return totalHorariosDisponiveis;
        }
        if (pontos >= 5) {
            return Math.min(totalHorariosDisponiveis, 8);
        }
        if (pontos >= 1) {
            return Math.min(totalHorariosDisponiveis, 5);
        }

        return Math.min(totalHorariosDisponiveis, 3);
    }

    private LocalDate parseData(String data) {
        if (data == null || data.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data e obrigatoria.");
        }

        try {
            return LocalDate.parse(data.trim());
        } catch (DateTimeParseException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data invalida. Use yyyy-MM-dd.");
        }
    }

    private HorarioResponseDTO toResponse(HorarioDisponivel horario) {
        return new HorarioResponseDTO(
                horario.getId(),
                horario.getBarbeiro().getNome(),
                horario.getData(),
                horario.getHora(),
                horario.getDisponivel()
        );
    }
}
