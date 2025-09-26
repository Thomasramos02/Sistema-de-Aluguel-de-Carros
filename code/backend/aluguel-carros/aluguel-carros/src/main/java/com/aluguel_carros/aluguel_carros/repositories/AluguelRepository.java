package com.aluguel_carros.aluguel_carros.repositories;

import com.aluguel_carros.aluguel_carros.model.Aluguel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AluguelRepository extends JpaRepository<Aluguel, Long> {
    
    List<Aluguel> findByClienteId(Long clienteId);
    
    List<Aluguel> findByCarroId(Long carroId);
    
    List<Aluguel> findByStatus(Aluguel.StatusAluguel status);
    
    @Query("SELECT a FROM Aluguel a WHERE a.cliente.id = :clienteId AND a.status = :status")
    List<Aluguel> findByClienteIdAndStatus(@Param("clienteId") Long clienteId, 
                                          @Param("status") Aluguel.StatusAluguel status);
    
    @Query("SELECT a FROM Aluguel a WHERE a.carro.id = :carroId AND a.status = :status")
    List<Aluguel> findByCarroIdAndStatus(@Param("carroId") Long carroId, 
                                        @Param("status") Aluguel.StatusAluguel status);
    
    @Query("SELECT a FROM Aluguel a WHERE a.dataInicio BETWEEN :dataInicio AND :dataFim")
    List<Aluguel> findByPeriodo(@Param("dataInicio") LocalDateTime dataInicio, 
                               @Param("dataFim") LocalDateTime dataFim);
    
    @Query("SELECT a FROM Aluguel a WHERE a.carro.id = :carroId AND " +
           "((a.dataInicio <= :dataFim AND a.dataFim >= :dataInicio) OR " +
           "(a.dataInicio <= :dataFim AND a.dataFim IS NULL)) AND " +
           "a.status = 'ATIVO'")
    List<Aluguel> findConflitosPorCarro(@Param("carroId") Long carroId, 
                                       @Param("dataInicio") LocalDateTime dataInicio, 
                                       @Param("dataFim") LocalDateTime dataFim);
    
    @Query("SELECT COUNT(a) FROM Aluguel a WHERE a.cliente.id = :clienteId AND a.status = 'ATIVO'")
    Long countAlugueisAtivosPorCliente(@Param("clienteId") Long clienteId);
}
