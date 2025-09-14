package com.aluguel_carros.aluguel_carros.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aluguel_carros.aluguel_carros.model.Cliente;

public interface  ClienteRepositories extends JpaRepository<Cliente, Long> {
    
    Optional<Cliente> findByCPF(String CPF);

    
}
