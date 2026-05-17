package com.devflows.barberflow.controlers;
import com.devflows.barberflow.service.ClienteService;
import com.devflows.barberflow.dto.ClienteRequestDTO;
import com.devflows.barberflow.dto.ClienteResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ClienteControler {
    private final ClienteService clienteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponseDTO cadastrarCliente(@RequestBody @Valid ClienteRequestDTO dto) {
        return clienteService.cadastrar(dto);
    }
    @GetMapping("/{id}")
    public ClienteResponseDTO listarClienteId(@PathVariable Long id){
        return clienteService.buscarPorId(id);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerClienteId(@PathVariable Long id){
        clienteService.excluirClientePorId(id);
    }
    @GetMapping
    public List<ClienteResponseDTO> listarPorNomeOuTelefone(@RequestParam(required = false) String busca){
        return clienteService.listarPorNomeOuTelefone(busca);
    }

    @PutMapping("/{id}")
    public ClienteResponseDTO atualizar(@PathVariable Long id, @RequestBody @Valid ClienteRequestDTO dto){
        return clienteService.atualizar(id,dto);
    }
}

