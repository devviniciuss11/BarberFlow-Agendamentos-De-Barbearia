package com.devflows.barberflow.service;

import com.devflows.barberflow.dto.AgendamentoRequestDTO;
import com.devflows.barberflow.dto.AgendamentoResponseDTO;
import com.devflows.barberflow.entity.Agendamento;
import com.devflows.barberflow.entity.Barbeiro;
import com.devflows.barberflow.entity.Cliente;
import com.devflows.barberflow.entity.HorarioDisponivel;
import com.devflows.barberflow.repository.AgendamentoRepository;
import com.devflows.barberflow.repository.BarbeiroRepository;
import com.devflows.barberflow.repository.ClienteRepository;
import com.devflows.barberflow.repository.HorarioDisponivelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final ClienteRepository clienteRepository;
    private final BarbeiroRepository barbeiroRepository;
    private final HorarioDisponivelRepository horarioDisponivelRepository;

    public AgendamentoResponseDTO agendar(AgendamentoRequestDTO dto) {
        Cliente cliente = buscarClientePorNomeTelefone(dto.nomeCliente(), dto.telefoneCliente());
        Barbeiro barbeiro = buscarBarbeiroPorTelefone(dto.telefoneBarbeiro());
        HorarioDisponivel horarioDisponivel = horarioDisponivelRepository
                .findByBarbeiroIdAndDataAndHoraAndDisponivelTrue(barbeiro.getId(), dto.data(), dto.hora())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT,
                        "Barbeiro nao possui horario disponivel cadastrado para esta data e horario."));

        boolean horarioOcupado = agendamentoRepository.existsByBarbeiroIdAndDataAndHorarioAndCanceladoFalse(
                barbeiro.getId(),
                dto.data(),
                dto.hora()
        );

        if (horarioOcupado) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ja existe um agendamento para este barbeiro nesse dia e horario.");
        }

        Agendamento agendamento = Agendamento.builder()
                .cliente(cliente)
                .barbeiro(barbeiro)
                .data(dto.data())
                .horario(dto.hora())
                .status(false)
                .cancelado(false)
                .build();

        Agendamento salvo = agendamentoRepository.save(agendamento);
        horarioDisponivel.setDisponivel(false);
        horarioDisponivelRepository.save(horarioDisponivel);

        return toResponseDTO(salvo);
    }

    public AgendamentoResponseDTO buscarPorId(Long id) {
        return toResponseDTO(buscarAgendamentoOuLancarErro(id));
    }

    public List<AgendamentoResponseDTO> buscarPorTelefoneBarbeiro(String telefoneBarbeiro) {
        validarTelefoneBarbeiro(telefoneBarbeiro);
        return agendamentoRepository.findByBarbeiroTelefoneOrderByDataAscHorarioAsc(telefoneBarbeiro.trim())
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<AgendamentoResponseDTO> listarDoBarbeiro(String telefoneBarbeiro) {
        validarTelefoneBarbeiro(telefoneBarbeiro);
        return agendamentoRepository.findByBarbeiroTelefoneAndCanceladoFalseAndStatusFalseOrderByDataAscHorarioAsc(telefoneBarbeiro.trim())
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public AgendamentoResponseDTO cancelarPorBarbeiro(Long agendamentoId, String senhaBarbeiro) {
        Agendamento agendamento = buscarAgendamentoOuLancarErro(agendamentoId);
        validarSenha(senhaBarbeiro, "Senha do barbeiro e obrigatoria.");

        if (!agendamento.getBarbeiro().getSenha().equals(senhaBarbeiro.trim())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Senha do barbeiro invalida.");
        }

        if (Boolean.TRUE.equals(agendamento.getCancelado())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Agendamento ja esta cancelado.");
        }

        agendamento.setCancelado(true);
        agendamento.setStatus(false);
        liberarHorario(agendamento);
        return toResponseDTO(agendamentoRepository.save(agendamento));
    }

    public AgendamentoResponseDTO cancelarPorCliente(Long agendamentoId, String senhaCliente) {
        Agendamento agendamento = buscarAgendamentoOuLancarErro(agendamentoId);
        validarSenha(senhaCliente, "Senha do cliente e obrigatoria.");

        if (!agendamento.getCliente().getSenha().equals(senhaCliente.trim())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Senha do cliente invalida.");
        }

        if (Boolean.TRUE.equals(agendamento.getCancelado())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Agendamento ja esta cancelado.");
        }

        agendamento.setCancelado(true);
        agendamento.setStatus(false);
        liberarHorario(agendamento);
        return toResponseDTO(agendamentoRepository.save(agendamento));
    }

    public AgendamentoResponseDTO concluirServico(Long agendamentoId, String senhaBarbeiro) {
        Agendamento agendamento = buscarAgendamentoOuLancarErro(agendamentoId);
        validarSenha(senhaBarbeiro, "Senha do barbeiro e obrigatoria.");

        if (!agendamento.getBarbeiro().getSenha().equals(senhaBarbeiro.trim())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Senha do barbeiro invalida.");
        }

        if (Boolean.TRUE.equals(agendamento.getCancelado())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Agendamento cancelado nao pode ser concluido.");
        }

        if (Boolean.TRUE.equals(agendamento.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Servico ja foi concluido.");
        }

        agendamento.setStatus(true);

        Cliente cliente = agendamento.getCliente();
        Integer pontos = cliente.getAgendamentopoints() == null ? 0 : cliente.getAgendamentopoints();
        cliente.setAgendamentopoints(pontos + 1);
        clienteRepository.save(cliente);

        return toResponseDTO(agendamentoRepository.save(agendamento));
    }

    private void validarSenha(String senha, String mensagem) {
        if (senha == null || senha.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, mensagem);
        }
    }

    private void validarTelefoneBarbeiro(String telefoneBarbeiro) {
        if (telefoneBarbeiro == null || telefoneBarbeiro.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Telefone do barbeiro e obrigatorio.");
        }

        if (!telefoneBarbeiro.trim().matches("\\d+")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Telefone do barbeiro deve conter apenas numeros.");
        }
    }

    private Agendamento buscarAgendamentoOuLancarErro(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Agendamento nao encontrado com id: " + id));
    }

    private Cliente buscarClientePorNomeTelefone(String nome, String telefone) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome do cliente e obrigatorio.");
        }
        if (telefone == null || telefone.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Telefone do cliente e obrigatorio.");
        }
        if (!telefone.trim().matches("\\d+")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Telefone do cliente deve conter apenas numeros.");
        }

        return clienteRepository.buscarClientesPorNomeOuTelefone(nome.trim())
                .stream()
                .filter(c -> c.getNome() != null && c.getTelefone() != null)
                .filter(c -> c.getNome().equalsIgnoreCase(nome.trim()) && c.getTelefone().equals(telefone.trim()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente nao encontrado com nome e telefone informados."));
    }

    private Barbeiro buscarBarbeiroPorTelefone(String telefoneBarbeiro) {
        if (telefoneBarbeiro == null || telefoneBarbeiro.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Telefone do barbeiro e obrigatorio.");
        }
        if (!telefoneBarbeiro.trim().matches("\\d+")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Telefone do barbeiro deve conter apenas numeros.");
        }

        return barbeiroRepository.findAll()
                .stream()
                .filter(b -> b.getTelefone() != null && b.getTelefone().equals(telefoneBarbeiro.trim()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Barbeiro nao encontrado com telefone informado."));
    }

    private AgendamentoResponseDTO toResponseDTO(Agendamento a) {
        return new AgendamentoResponseDTO(
                a.getId(),
                a.getCliente().getNome(),
                a.getCliente().getTelefone(),
                a.getBarbeiro().getNome(),
                a.getBarbeiro().getTelefone(),
                a.getData(),
                a.getHorario(),
                a.getStatus(),
                a.getCancelado()
        );
    }
    private void liberarHorario(Agendamento agendamento) {
        horarioDisponivelRepository
                .findByBarbeiroIdAndDataAndHoraAndDisponivelFalse(
                        agendamento.getBarbeiro().getId(),
                        agendamento.getData(),
                        agendamento.getHorario()
                )
                .ifPresent(horario -> {
                    horario.setDisponivel(true);
                    horarioDisponivelRepository.save(horario);
                });
    }
}
