package com.devflows.barberflow.controller;

import com.devflows.barberflow.dto.AgendamentoRequestDTO;
import com.devflows.barberflow.dto.AgendamentoResponseDTO;
import com.devflows.barberflow.service.AgendamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agendamentos")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AgendamentoControler {

    private final AgendamentoService agendamentoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AgendamentoResponseDTO agendar(@RequestBody @Valid AgendamentoRequestDTO dto) {
        return agendamentoService.agendar(dto);
    }

    @GetMapping("/{id}")
    public AgendamentoResponseDTO buscarPorId(@PathVariable Long id) {
        return agendamentoService.buscarPorId(id);
    }

    @GetMapping("/barbeiro/buscar")
    public List<AgendamentoResponseDTO> buscarPorTelefoneBarbeiro(@RequestParam String telefoneBarbeiro) {
        return agendamentoService.buscarPorTelefoneBarbeiro(telefoneBarbeiro);
    }

    @GetMapping("/barbeiro/listar")
    public List<AgendamentoResponseDTO> listarDoBarbeiro(@RequestParam String telefoneBarbeiro) {
        return agendamentoService.listarDoBarbeiro(telefoneBarbeiro);
    }

    @PatchMapping("/{id}/cancelar/barbeiro")
    public AgendamentoResponseDTO cancelarPorBarbeiro(
            @PathVariable Long id,
            @RequestParam String senhaBarbeiro
    ) {
        return agendamentoService.cancelarPorBarbeiro(id, senhaBarbeiro);
    }

    @PatchMapping("/{id}/cancelar/cliente")
    public AgendamentoResponseDTO cancelarPorCliente(
            @PathVariable Long id,
            @RequestParam String senhaCliente
    ) {
        return agendamentoService.cancelarPorCliente(id, senhaCliente);
    }

    @PatchMapping("/{id}/concluir")
    public AgendamentoResponseDTO concluirServico(
            @PathVariable Long id,
            @RequestParam String senhaBarbeiro
    ) {
        return agendamentoService.concluirServico(id, senhaBarbeiro);
    }
}
