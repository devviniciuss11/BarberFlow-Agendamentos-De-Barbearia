package com.devflows.barberflow.controller;

import com.devflows.barberflow.dto.BarbeiroRequestDTO;
import com.devflows.barberflow.dto.BarbeiroResponseDTO;
import com.devflows.barberflow.service.BarbeiroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/barbeiros")
@RequiredArgsConstructor

public class BarbeiroControler {

    private final BarbeiroService barbeiroService;

    @PostMapping
    public ResponseEntity<BarbeiroResponseDTO> cadastrar(@RequestBody @Valid BarbeiroRequestDTO dto) {
        BarbeiroResponseDTO response = barbeiroService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarbeiroResponseDTO> buscarPorId(@PathVariable Long id) {
        BarbeiroResponseDTO response = barbeiroService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BarbeiroResponseDTO>> listarTodos() {
        List<BarbeiroResponseDTO> response = barbeiroService.listarTodos();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BarbeiroResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid BarbeiroRequestDTO dto) {
        BarbeiroResponseDTO response = barbeiroService.atualizar(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        barbeiroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

