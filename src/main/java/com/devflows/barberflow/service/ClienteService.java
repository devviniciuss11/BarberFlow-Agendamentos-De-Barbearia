package com.devflows.barberflow.service;

import com.devflows.barberflow.dto.ClienteResponseDTO;
import com.devflows.barberflow.entity.Cliente;
import com.devflows.barberflow.repository.ClienteRepository;
import com.devflows.barberflow.dto.ClienteRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository clienteRepository;
    public ClienteResponseDTO cadastrar(ClienteRequestDTO dto){
        Cliente cliente = Cliente.builder().nome(dto.nome()).telefone(dto.telefone()).senha(dto.senha()).agendamentopoints(0).build();
        if(clienteRepository.existsBytelefone(dto.telefone())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Telefone Já Cadastrado");
        }
        return toResponse(clienteRepository.save(cliente));

    }
    public ClienteResponseDTO buscarPorId(Long id){
        return toResponse(buscarEntidade(id));
    }
    public void excluirClientePorId(Long id){
        if(!clienteRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente Não Encontrado");
        }
        clienteRepository.deleteById(id);
    }

    private Cliente buscarEntidade(Long id){
        return clienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    private ClienteResponseDTO toResponse(Cliente cliente) {
        return new ClienteResponseDTO(cliente.getId(), cliente.getNome(), cliente.getTelefone(), cliente.getAgendamentopoints());
    }
    public List<ClienteResponseDTO> listarPorNomeOuTelefone(String busca){
        boolean temBusca = busca != null && !busca.isBlank();
        String termoBusca = temBusca ? busca.trim() : null;

        List<Cliente> clientes = temBusca
                ? clienteRepository.buscarClientesPorNome(termoBusca)
                : clienteRepository.findAll();

        if (temBusca && clientes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Nenhum cliente encontrado para nome ou telefone: " + termoBusca);
        }

        return clientes.stream().map(this::toResponse).toList();
    }


    public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO dto) {
        Cliente cliente = buscarEntidade(id);
        String novoTelefone = dto.telefone() == null ? null : dto.telefone().trim();

        if (novoTelefone != null
                && !novoTelefone.isBlank()
                && clienteRepository.existsByTelefoneAndIdNot(novoTelefone, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Telefone ja cadastrado");
        }

        cliente.setNome(dto.nome());
        cliente.setTelefone(novoTelefone);
        cliente.setSenha(dto.senha());
        return toResponse(clienteRepository.save(cliente));
    }

}

