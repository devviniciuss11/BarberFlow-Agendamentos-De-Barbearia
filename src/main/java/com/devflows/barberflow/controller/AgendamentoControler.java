package com.devflows.barberflow.controller;

import com.devflows.barberflow.dto.AgendamentoRequestDTO;
import com.devflows.barberflow.dto.AgendamentoResponseDTO;
import com.devflows.barberflow.service.AgendamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agendamentos")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Agendamentos", description = "Operacoes de criacao, consulta, cancelamento e conclusao de agendamentos.")
public class AgendamentoControler {

    private final AgendamentoService agendamentoService;

    @Operation(
            summary = "Criar agendamento",
            description = "Cria um agendamento para cliente e barbeiro em data/hora especificas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Agendamento criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Payload invalido."),
            @ApiResponse(responseCode = "404", description = "Cliente ou barbeiro nao encontrado."),
            @ApiResponse(responseCode = "409", description = "Horario indisponivel ou conflito de agendamento.")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AgendamentoResponseDTO agendar(@RequestBody @Valid AgendamentoRequestDTO dto) {
        return agendamentoService.agendar(dto);
    }

    @Operation(
            summary = "Buscar agendamento por ID",
            description = "Retorna um agendamento especifico."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agendamento encontrado."),
            @ApiResponse(responseCode = "404", description = "Agendamento nao encontrado.")
    })
    @GetMapping("/{id}")
    public AgendamentoResponseDTO buscarPorId(
            @Parameter(description = "ID do agendamento.", example = "1")
            @PathVariable Long id
    ) {
        return agendamentoService.buscarPorId(id);
    }

    @Operation(
            summary = "Buscar agendamentos por telefone do barbeiro",
            description = "Retorna todos os agendamentos relacionados ao barbeiro informado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Telefone do barbeiro invalido.")
    })
    @GetMapping("/barbeiro/buscar")
    public List<AgendamentoResponseDTO> buscarPorTelefoneBarbeiro(
            @Parameter(description = "Telefone do barbeiro (somente numeros).", example = "11999990001")
            @RequestParam String telefoneBarbeiro
    ) {
        return agendamentoService.buscarPorTelefoneBarbeiro(telefoneBarbeiro);
    }

    @Operation(
            summary = "Listar agendamentos ativos do barbeiro",
            description = "Lista apenas agendamentos nao cancelados e nao concluidos para o barbeiro informado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Telefone do barbeiro invalido.")
    })
    @GetMapping("/barbeiro/listar")
    public List<AgendamentoResponseDTO> listarDoBarbeiro(
            @Parameter(description = "Telefone do barbeiro (somente numeros).", example = "11999990001")
            @RequestParam String telefoneBarbeiro
    ) {
        return agendamentoService.listarDoBarbeiro(telefoneBarbeiro);
    }

    @Operation(
            summary = "Cancelar agendamento pelo barbeiro",
            description = "Cancela um agendamento validando a senha do barbeiro."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agendamento cancelado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Senha obrigatoria."),
            @ApiResponse(responseCode = "401", description = "Senha do barbeiro invalida."),
            @ApiResponse(responseCode = "404", description = "Agendamento nao encontrado."),
            @ApiResponse(responseCode = "409", description = "Agendamento ja cancelado.")
    })
    @PatchMapping("/{id}/cancelar/barbeiro")
    public AgendamentoResponseDTO cancelarPorBarbeiro(
            @Parameter(description = "ID do agendamento.", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Senha do barbeiro.", example = "senhaB")
            @RequestParam String senhaBarbeiro
    ) {
        return agendamentoService.cancelarPorBarbeiro(id, senhaBarbeiro);
    }

    @Operation(
            summary = "Cancelar agendamento pelo cliente",
            description = "Cancela um agendamento validando a senha do cliente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agendamento cancelado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Senha obrigatoria."),
            @ApiResponse(responseCode = "401", description = "Senha do cliente invalida."),
            @ApiResponse(responseCode = "404", description = "Agendamento nao encontrado."),
            @ApiResponse(responseCode = "409", description = "Agendamento ja cancelado.")
    })
    @PatchMapping("/{id}/cancelar/cliente")
    public AgendamentoResponseDTO cancelarPorCliente(
            @Parameter(description = "ID do agendamento.", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Senha do cliente.", example = "senhaC")
            @RequestParam String senhaCliente
    ) {
        return agendamentoService.cancelarPorCliente(id, senhaCliente);
    }

    @Operation(
            summary = "Concluir agendamento",
            description = "Marca o agendamento como concluido e incrementa pontos do cliente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Servico concluido com sucesso."),
            @ApiResponse(responseCode = "400", description = "Senha obrigatoria."),
            @ApiResponse(responseCode = "401", description = "Senha do barbeiro invalida."),
            @ApiResponse(responseCode = "404", description = "Agendamento nao encontrado."),
            @ApiResponse(responseCode = "409", description = "Agendamento cancelado ou ja concluido.")
    })
    @PatchMapping("/{id}/concluir")
    public AgendamentoResponseDTO concluirServico(
            @Parameter(description = "ID do agendamento.", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Senha do barbeiro.", example = "senhaB")
            @RequestParam String senhaBarbeiro
    ) {
        return agendamentoService.concluirServico(id, senhaBarbeiro);
    }
}
