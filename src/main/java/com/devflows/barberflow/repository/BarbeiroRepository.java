package com.devflows.barberflow.repository;

import com.devflows.barberflow.entity.Barbeiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

@Repository
public interface BarbeiroRepository extends JpaRepository<Barbeiro, Long> {

    List<Barbeiro> findByEspecialidade(String especialidade);

    Optional<Barbeiro> findByTelefone(String telefone);

    boolean existsByTelefone(String telefone);

    boolean existsByCpf(String cpf);

    boolean existsByTelefoneAndIdNot(String telefone, Long id);

    boolean existsByCpfAndIdNot(String cpf, Long id);

    @Query(value = """
            SELECT b.id, b.especialidade, b.telefone, b.nome, b.cpf, b.senha
            FROM barbeiro b
            WHERE b.nome ILIKE CONCAT('%', :nome, '%')
            ORDER BY b.nome
            """, nativeQuery = true)
    List<Barbeiro> buscarPorNome(@Param("nome") String nome);
}
