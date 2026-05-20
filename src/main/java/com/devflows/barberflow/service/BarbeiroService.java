package com.devflows.barberflow.service;

import com.devflows.barberflow.dto.BarbeiroRequestDTO;
import com.devflows.barberflow.dto.BarbeiroResponseDTO;
import com.devflows.barberflow.entity.Barbeiro;
import com.devflows.barberflow.repository.BarbeiroRepository;
import com.devflows.barberflow.repository.HorarioDisponivelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BarbeiroService {
    private final BarbeiroRepository barbeiroRepository;
    private final HorarioDisponivelRepository horarioDisponivelRepository;

    public BarbeiroResponseDTO cadastrar(BarbeiroRequestDTO dto) {
        if (barbeiroRepository.existsByTelefone(dto.telefone())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Telefone ja cadastrado.");
        }

        if (barbeiroRepository.existsByCpf(dto.cpf())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cpf ja cadastrado.");
        }

        Barbeiro barbeiro = Barbeiro.builder()
                .nome(dto.nome())
                .especialidade(dto.especialidade())
                .telefone(dto.telefone())
                .senha(dto.senha())
                .cpf(dto.cpf())
                .build();

        return toResponse(barbeiroRepository.save(barbeiro));
    }

    public BarbeiroResponseDTO buscarPorId(Long id) {
        Barbeiro barbeiro = barbeiroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Barbeiro nao encontrado. ID: " + id));
        return toResponse(barbeiro);
    }

    public List<BarbeiroResponseDTO> listarTodos() {
        return barbeiroRepository.buscarPorNome("")
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public BarbeiroResponseDTO atualizar(Long id, BarbeiroRequestDTO dto) {
        Barbeiro barbeiro = barbeiroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Barbeiro nao encontrado. ID: " + id));

        String novoTelefone = dto.telefone() == null ? null : dto.telefone().trim();
        String novoCpf = dto.cpf() == null ? null : dto.cpf().trim();

        if (novoTelefone != null
                && !novoTelefone.isBlank()
                && barbeiroRepository.existsByTelefoneAndIdNot(novoTelefone, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Telefone ja cadastrado.");
        }

        if (novoCpf != null
                && !novoCpf.isBlank()
                && barbeiroRepository.existsByCpfAndIdNot(novoCpf, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cpf ja cadastrado.");
        }

        barbeiro.setNome(dto.nome());
        barbeiro.setEspecialidade(dto.especialidade());
        barbeiro.setTelefone(novoTelefone);
        barbeiro.setCpf(novoCpf);
        barbeiro.setSenha(dto.senha());

        return toResponse(barbeiroRepository.save(barbeiro));
    }

    @Transactional
    public void deletar(Long id) {
        Barbeiro barbeiro = barbeiroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Barbeiro nao encontrado. ID: " + id));

        horarioDisponivelRepository.deleteByBarbeiroId(barbeiro.getId());
        barbeiroRepository.delete(barbeiro);
    }

    private BarbeiroResponseDTO toResponse(Barbeiro barbeiro) {
        return new BarbeiroResponseDTO(
                barbeiro.getId(),
                barbeiro.getEspecialidade(),
                barbeiro.getNome(),
                barbeiro.getTelefone(),
                barbeiro.getCpf()
        );
    }
}
