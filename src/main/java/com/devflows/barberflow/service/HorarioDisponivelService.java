package com.devflows.barberflow.service;

import com.devflows.barberflow.entity.Barbeiro;
import com.devflows.barberflow.entity.HorarioDisponivel;
import com.devflows.barberflow.repositorys.HorarioDisponivelRepository;
import com.devflows.barberflow.dto.HorarioRequestDTO;
import com.devflows.barberflow.dto.HorarioResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HorarioDisponivelService {
    private final HorarioDisponivelRepository horarioDisponivelRepository;
    private final com.devflows.barberflow.repositorys.BarbeiroRepository barbeiroRepository;

    public HorarioResponseDTO cadastrarHorario(HorarioRequestDTO dto) {
        if (dto.barbeiroid() == null) throw new RuntimeException("Informe o ID do barbeiro.");
        if (dto.data() == null) throw new RuntimeException("Informe a data do horário");
        if (dto.hora() == null) throw new RuntimeException("Informe a hora do Horário");

        Barbeiro barbeiro = barbeiroRepository.findById(dto.barbeiroid())
                .orElseThrow(()-> new RuntimeException("Barbeiro não encontrado."));


        if (horarioDisponivelRepository.buscarHorarioPorBarbeiroDataHora(dto.barbeiroid(), dto.data(), dto.hora()).isPresent()) {
            throw new RuntimeException("Este horário já foi cadastrado para esse barbeiro.");
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
        LocalDate dataConvertida = LocalDate.parse(data);
        return horarioDisponivelRepository.buscarHorariosDisponiveis(barbeiroId, dataConvertida)
                .stream().map(this::toResponse).toList();
    }

    private HorarioResponseDTO toResponse(HorarioDisponivel horario) {
        return new HorarioResponseDTO(horario.getId(), horario.getBarbeiro().getNome(), horario.getData(), horario.getHora(), horario.getDisponivel());
    }
}
