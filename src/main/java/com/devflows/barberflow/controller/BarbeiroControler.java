package com.devflows.barberflow.controller;

import com.devflows.barberflow.dto.BarbeiroRequestDTO;
import com.devflows.barberflow.dto.BarbeiroResponseDTO;
import com.devflows.barberflow.service.BarbeiroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/barbeiros")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Barbeiros", description = "Operacoes de cadastro, consulta, atualizacao e inativacao de barbeiros.")
public class BarbeiroControler {

    private final BarbeiroService barbeiroService;

    @Operation(
            summary = "Cadastrar barbeiro",
            description = "Cria um novo barbeiro com nome, especialidade, telefone, cpf e senha."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Barbeiro cadastrado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Payload invalido."),
            @ApiResponse(responseCode = "409", description = "Telefone ou CPF ja cadastrado.")
    })
    @PostMapping
    public ResponseEntity<BarbeiroResponseDTO> cadastrar(@RequestBody @Valid BarbeiroRequestDTO dto) {
        BarbeiroResponseDTO response = barbeiroService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Buscar barbeiro por ID",
            description = "Retorna os dados de um barbeiro especifico."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Barbeiro encontrado."),
            @ApiResponse(responseCode = "404", description = "Barbeiro nao encontrado.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BarbeiroResponseDTO> buscarPorId(
            @Parameter(description = "ID do barbeiro.", example = "1")
            @PathVariable Long id
    ) {
        BarbeiroResponseDTO response = barbeiroService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Listar barbeiros",
            description = "Retorna a lista de barbeiros cadastrados."
    )
    @ApiResponse(responseCode = "200", description = "Lista de barbeiros retornada com sucesso.")
    @GetMapping
    public ResponseEntity<List<BarbeiroResponseDTO>> listarTodos() {
        List<BarbeiroResponseDTO> response = barbeiroService.listarTodos();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Atualizar barbeiro",
            description = "Atualiza os dados de um barbeiro existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Barbeiro atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Payload invalido."),
            @ApiResponse(responseCode = "404", description = "Barbeiro nao encontrado."),
            @ApiResponse(responseCode = "409", description = "Telefone ou CPF ja cadastrado para outro barbeiro.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BarbeiroResponseDTO> atualizar(
            @Parameter(description = "ID do barbeiro.", example = "1")
            @PathVariable Long id,
            @RequestBody @Valid BarbeiroRequestDTO dto
    ) {
        BarbeiroResponseDTO response = barbeiroService.atualizar(id, dto);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Inativar barbeiro",
            description = "Realiza inativacao logica do barbeiro (campo ativo = false)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Barbeiro inativado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Barbeiro nao encontrado.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do barbeiro.", example = "1")
            @PathVariable Long id
    ) {
        barbeiroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
