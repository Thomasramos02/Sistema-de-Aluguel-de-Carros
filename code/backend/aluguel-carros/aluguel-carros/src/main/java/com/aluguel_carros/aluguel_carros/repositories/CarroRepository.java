package com.aluguel_carros.aluguel_carros.repositories;

import com.aluguel_carros.aluguel_carros.model.Carro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarroRepository extends JpaRepository<Carro, Long> {
    
    Optional<Carro> findByPlaca(String placa);
    
    List<Carro> findByStatus(Carro.StatusCarro status);
    
    List<Carro> findByMarca(String marca);
    
    List<Carro> findByModelo(String modelo);
    
    @Query("SELECT c FROM Carro c WHERE c.status = :status AND c.valorDiaria <= :valorMaximo")
    List<Carro> findDisponiveisPorValorMaximo(@Param("status") Carro.StatusCarro status, 
                                            @Param("valorMaximo") java.math.BigDecimal valorMaximo);
    
    @Query("SELECT c FROM Carro c WHERE c.status = 'DISPONIVEL' AND " +
           "(LOWER(c.marca) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(c.modelo) LIKE LOWER(CONCAT('%', :termo, '%')))")
    List<Carro> buscarDisponiveisPorTermo(@Param("termo") String termo);
    
    boolean existsByPlaca(String placa);
}
