package com.aluguel_carros.aluguel_carros.repositories;

import com.aluguel_carros.aluguel_carros.model.PedidoAluguel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoAluguelRepository extends JpaRepository<PedidoAluguel, Long> {
    
    List<PedidoAluguel> findByClienteId(Long clienteId);
    
    List<PedidoAluguel> findByCarroId(Long carroId);
    
    List<PedidoAluguel> findByAgenteId(Long agenteId);
    
    List<PedidoAluguel> findByStatus(PedidoAluguel.StatusPedido status);
    
    List<PedidoAluguel> findByStatusOrderByDataSolicitacaoDesc(PedidoAluguel.StatusPedido status);
    
    Optional<PedidoAluguel> findByIdAndClienteId(Long id, Long clienteId);
}
