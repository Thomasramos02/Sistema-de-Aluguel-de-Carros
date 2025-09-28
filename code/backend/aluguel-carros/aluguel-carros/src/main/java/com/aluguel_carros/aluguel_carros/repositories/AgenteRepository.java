package com.aluguel_carros.aluguel_carros.repositories;

import com.aluguel_carros.aluguel_carros.model.Agente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgenteRepository extends JpaRepository<Agente, Long> {
    
    Optional<Agente> findByEmail(String email);
    
    Optional<Agente> findByCodigoAgente(String codigoAgente);
    
    boolean existsByEmail(String email);
    
    boolean existsByCodigoAgente(String codigoAgente);
}
