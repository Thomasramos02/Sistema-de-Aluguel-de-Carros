package com.aluguel_carros.aluguel_carros.repositories;

import com.aluguel_carros.aluguel_carros.model.UsuarioAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioAuthRepository extends JpaRepository<UsuarioAuth, Long> {
    
    Optional<UsuarioAuth> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    Optional<UsuarioAuth> findByEmailAndAtivoTrue(String email);
}
