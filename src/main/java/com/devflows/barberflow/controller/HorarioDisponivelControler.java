package com.devflows.barberflow.controller;

import com.devflows.barberflow.dto.HorarioRequestDTO;
import com.devflows.barberflow.dto.HorarioResponseDTO;
import com.devflows.barberflow.service.HorarioDisponivelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Horarios")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Horarios", description = "Operacoes de cadastro e consulta de horarios disponiveis por barbeiro.")
public class HorarioDisponivelControler {
    private final HorarioDisponivelService horarioservice;

    @Operation(
            summary = "Cadastrar horario disponivel",
            description = "Cria um novo horario disponivel para um barbeiro em data e hora especificas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Horario cadastrado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Payload invalido."),
            @ApiResponse(responseCode = "404", description = "Barbeiro nao encontrado."),
            @ApiResponse(responseCode = "409", description = "Horario ja cadastrado para o barbeiro.")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HorarioResponseDTO cadastrarHorario(@RequestBody HorarioRequestDTO dto) {
        return horarioservice.cadastrarHorario(dto);
    }

    @Operation(
            summary = "Listar horarios disponiveis",
            description = "Retorna horarios disponiveis para um barbeiro em uma data, respeitando o limite por pontos do cliente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Parametros invalidos."),
            @ApiResponse(responseCode = "404", description = "Cliente nao encontrado.")
    })
    @GetMapping("/disponiveis")
    public List<HorarioResponseDTO> listarDisponiveis(
            @Parameter(description = "ID do barbeiro.", example = "1")
            @RequestParam Long barbeiroId,
            @Parameter(description = "ID do cliente.", example = "1")
            @RequestParam Long clienteId,
            @Parameter(description = "Data no formato yyyy-MM-dd.", example = "2026-12-31")
            @RequestParam String data
    ) {
        return horarioservice.listarDisponiveis(barbeiroId, clienteId, data);
    }
}
