package com.devflows.barberflow.service;

import com.devflows.barberflow.dto.BarbeiroRequestDTO;
import com.devflows.barberflow.dto.BarbeiroResponseDTO;
import com.devflows.barberflow.entity.Barbeiro;
import com.devflows.barberflow.repository.BarbeiroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BarbeiroService {
    private final BarbeiroRepository barbeiroRepository;

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
                .ativo(true)
                .build();

        return toResponse(barbeiroRepository.save(barbeiro));
    }

    public BarbeiroResponseDTO buscarPorId(Long id) {
        Barbeiro barbeiro = barbeiroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Barbeiro nao encontrado. ID: " + id));
        return toResponse(barbeiro);
    }

    public List<BarbeiroResponseDTO> listarTodos() {
        return barbeiroRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
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

    public void deletar(Long id) {
        Barbeiro barbeiro = barbeiroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Barbeiro nao encontrado. ID: " + id));

        barbeiro.setAtivo(false);
        barbeiroRepository.save(barbeiro);
    }

    private BarbeiroResponseDTO toResponse(Barbeiro barbeiro) {
        return new BarbeiroResponseDTO(
                barbeiro.getId(),
                barbeiro.getEspecialidade(),
                barbeiro.getNome(),
                barbeiro.getTelefone(),
                barbeiro.getCpf(),
                barbeiro.getAtivo()
        );
    }
}
