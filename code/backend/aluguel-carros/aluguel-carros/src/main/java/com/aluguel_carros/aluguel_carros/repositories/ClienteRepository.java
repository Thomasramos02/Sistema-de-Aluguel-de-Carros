package com.aluguel_carros.aluguel_carros.repositories;

import com.aluguel_carros.aluguel_carros.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    Optional<Cliente> findByEmail(String email);
    
    Optional<Cliente> findByCpf(String cpf);
    
    Optional<Cliente> findByRg(String rg);
    
    @Query("SELECT c FROM Cliente c WHERE LOWER(c.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Cliente> findByNomeContainingIgnoreCase(@Param("nome") String nome);
    
    @Query("SELECT c FROM Cliente c WHERE LOWER(c.profissao) LIKE LOWER(CONCAT('%', :profissao, '%'))")
    List<Cliente> findByProfissaoContainingIgnoreCase(@Param("profissao") String profissao);
    
    boolean existsByEmail(String email);
    
    boolean existsByCpf(String cpf);
    
    boolean existsByRg(String rg);
}
