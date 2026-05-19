package com.devflows.barberflow.controller;

import com.devflows.barberflow.dto.ClienteRequestDTO;
import com.devflows.barberflow.dto.ClienteResponseDTO;
import com.devflows.barberflow.service.ClienteService;
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
@RequestMapping("/clientes")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Clientes", description = "Operacoes de cadastro, consulta, atualizacao e remocao de clientes.")
public class ClienteControler {
    private final ClienteService clienteService;

    @Operation(
            summary = "Cadastrar cliente",
            description = "Cria um novo cliente na base com nome, telefone e senha."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Payload invalido."),
            @ApiResponse(responseCode = "409", description = "Telefone ja cadastrado.")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponseDTO cadastrarCliente(@RequestBody @Valid ClienteRequestDTO dto) {
        return clienteService.cadastrar(dto);
    }

    @Operation(
            summary = "Buscar cliente por ID",
            description = "Retorna os dados de um cliente especifico."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado."),
            @ApiResponse(responseCode = "404", description = "Cliente nao encontrado.")
    })
    @GetMapping("/{id}")
    public ClienteResponseDTO listarClienteId(
            @Parameter(description = "ID do cliente.", example = "1")
            @PathVariable Long id
    ) {
        return clienteService.buscarPorId(id);
    }

    @Operation(
            summary = "Remover cliente",
            description = "Exclui o cliente pelo ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente removido com sucesso."),
            @ApiResponse(responseCode = "404", description = "Cliente nao encontrado.")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerClienteId(
            @Parameter(description = "ID do cliente.", example = "1")
            @PathVariable Long id
    ) {
        clienteService.excluirClientePorId(id);
    }

    @Operation(
            summary = "Listar clientes",
            description = "Lista todos os clientes ou filtra por nome/telefone usando o parametro opcional 'busca'."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Nenhum cliente encontrado para o filtro.")
    })
    @GetMapping
    public List<ClienteResponseDTO> listarPorNomeOuTelefone(
            @Parameter(description = "Filtro opcional para nome ou telefone.", example = "11988880001")
            @RequestParam(required = false) String busca
    ) {
        return clienteService.listarPorNomeOuTelefone(busca);
    }

    @Operation(
            summary = "Atualizar cliente",
            description = "Atualiza nome, telefone e senha de um cliente existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Payload invalido."),
            @ApiResponse(responseCode = "404", description = "Cliente nao encontrado."),
            @ApiResponse(responseCode = "409", description = "Telefone ja cadastrado para outro cliente.")
    })
    @PutMapping("/{id}")
    public ClienteResponseDTO atualizar(
            @Parameter(description = "ID do cliente.", example = "1")
            @PathVariable Long id,
            @RequestBody @Valid ClienteRequestDTO dto
    ) {
        return clienteService.atualizar(id, dto);
    }
}
