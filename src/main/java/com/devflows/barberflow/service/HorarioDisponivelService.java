package com.devflows.barberflow.service;

import com.devflows.barberflow.dto.HorarioRequestDTO;
import com.devflows.barberflow.dto.HorarioResponseDTO;
import com.devflows.barberflow.entity.Barbeiro;
import com.devflows.barberflow.entity.HorarioDisponivel;
import com.devflows.barberflow.repositorys.BarbeiroRepository;
import com.devflows.barberflow.repositorys.HorarioDisponivelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HorarioDisponivelService {

    private final HorarioDisponivelRepository horarioDisponivelRepository;
    private final BarbeiroRepository barbeiroRepository;

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

    public List<HorarioResponseDTO> listarDisponiveis(Long barbeiroId, String data) {
        if (barbeiroId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID do barbeiro e obrigatorio.");
        }

        LocalDate dataConvertida = parseData(data);
        return horarioDisponivelRepository.findByBarbeiroIdAndDataAndDisponivelTrueOrderByHoraAsc(barbeiroId, dataConvertida)
                .stream()
                .map(this::toResponse)
                .toList();
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
