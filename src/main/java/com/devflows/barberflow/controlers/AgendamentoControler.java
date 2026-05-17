package com.devflows.barberflow.controlers;
import com.devflows.barberflow.entity.Agendamento.StatusAgendamento;
import com.devflows.barberflow.service.AgendamentoService;
import com.devflows.barberflow.dto.AgendamentoRequestDTO;
import com.devflows.barberflow.dto.AgendamentoResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/agendamentos")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AgendamentoControler {


    @RestController
    @RequestMapping("/agendamentos")
    @RequiredArgsConstructor
    @CrossOrigin("*")
    public class AgendamentoController {

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

        @GetMapping
        public List<AgendamentoResponseDTO> listar(
                @RequestParam(required = false) Long clienteId,
                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                @RequestParam(required = false) StatusAgendamento status
        ) {
            return agendamentoService.listarComFiltros(clienteId, data, status);
        }

        @PutMapping("/{id}")
        public AgendamentoResponseDTO atualizar(
                @PathVariable Long id,
                @RequestBody @Valid AgendamentoRequestDTO dto
        ) {
            return agendamentoService.atualizar(id, dto);
        }

        @PatchMapping("/{id}/status")
        public AgendamentoResponseDTO atualizarStatus(
                @PathVariable Long id,
                @RequestParam StatusAgendamento status
        ) {
            return agendamentoService.atualizarStatus(id, status);
        }

        @DeleteMapping("/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void excluir(@PathVariable Long id) {
            agendamentoService.excluir(id);
        }
    }
}

