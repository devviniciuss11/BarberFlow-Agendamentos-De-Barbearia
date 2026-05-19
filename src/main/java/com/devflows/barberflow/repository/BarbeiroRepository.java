package com.devflows.barberflow.repository;

import com.devflows.barberflow.entity.Barbeiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface BarbeiroRepository extends JpaRepository<Barbeiro, Long> {

    List<Barbeiro> findByAtivoTrueOrderByNomeAsc();

    List<Barbeiro> findByEspecialidade(String especialidade);

    boolean existsByTelefone(String telefone);

    boolean existsByCpf(String cpf);

    boolean existsByTelefoneAndIdNot(String telefone, Long id);

    boolean existsByCpfAndIdNot(String cpf, Long id);

    @Query("SELECT b FROM Barbeiro b WHERE LOWER(b.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Barbeiro> buscarPorNome(@Param("nome") String nome);
}

