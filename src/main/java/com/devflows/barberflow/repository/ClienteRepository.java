package com.devflows.barberflow.repository;

import com.devflows.barberflow.entity.Cliente;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    @Query(value = """
            SELECT c.id, c.nome, c.telefone, c.senha, c.agendamentopoints
            FROM cliente c
            WHERE LOWER(c.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
               OR c.telefone LIKE CONCAT('%', :nome, '%')
            ORDER BY c.nome
            """, nativeQuery = true)
    List<Cliente> buscarClientesPorNomeOuTelefone(@Param("nome") String nome);


    boolean existsByTelefone(@NotBlank(message = "Telefone é obrigatório.") String telefone);
    boolean existsByTelefoneAndIdNot(String telefone, Long id);
}

