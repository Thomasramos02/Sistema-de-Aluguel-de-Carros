package com.aluguel_carros.aluguel_carros.services;

import com.aluguel_carros.aluguel_carros.model.Agente;
import com.aluguel_carros.aluguel_carros.model.Aluguel;
import com.aluguel_carros.aluguel_carros.model.Carro;
import com.aluguel_carros.aluguel_carros.model.Cliente;
import com.aluguel_carros.aluguel_carros.model.PedidoAluguel;
import com.aluguel_carros.aluguel_carros.repositories.PedidoAluguelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PedidoAluguelService {
    
    private final PedidoAluguelRepository pedidoAluguelRepository;
    private final ClienteService clienteService;
    private final CarroService carroService;
    private final AluguelService aluguelService;
    private final AgenteService agenteService;
    
    public PedidoAluguel solicitarAluguel(Long clienteId, Long carroId, 
                                         LocalDateTime dataInicio, LocalDateTime dataFim,
                                         String observacoesCliente) {
        
        Cliente cliente = clienteService.buscarPorId(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        Carro carro = carroService.buscarPorId(carroId)
            .orElseThrow(() -> new RuntimeException("Carro não encontrado"));
        
        if (carro.getStatus() != Carro.StatusCarro.DISPONIVEL) {
            throw new RuntimeException("Carro não está disponível para aluguel");
        }
        
        if (dataInicio.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Data de início não pode ser no passado");
        }
        
        if (dataFim.isBefore(dataInicio)) {
            throw new RuntimeException("Data de fim deve ser posterior à data de início");
        }
        
        // Calcular valor total
        long dias = ChronoUnit.DAYS.between(dataInicio, dataFim);
        BigDecimal valorTotal = carro.getValorDiaria().multiply(BigDecimal.valueOf(dias));
        
        PedidoAluguel pedido = PedidoAluguel.builder()
            .cliente(cliente)
            .carro(carro)
            .dataInicio(dataInicio)
            .dataFim(dataFim)
            .valorTotal(valorTotal)
            .observacoesCliente(observacoesCliente)
            .status(PedidoAluguel.StatusPedido.PENDENTE)
            .build();
        
        return pedidoAluguelRepository.save(pedido);
    }
    
    public PedidoAluguel aprovarPedido(Long pedidoId, Long agenteId, String observacoesAgente) {
        PedidoAluguel pedido = pedidoAluguelRepository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        
        if (pedido.getStatus() != PedidoAluguel.StatusPedido.PENDENTE) {
            throw new RuntimeException("Pedido já foi analisado");
        }
        
        Agente agente = agenteService.buscarPorId(agenteId)
            .orElseThrow(() -> new RuntimeException("Agente não encontrado"));
        
        pedido.setStatus(PedidoAluguel.StatusPedido.APROVADO);
        pedido.setAgente(agente);
        pedido.setObservacoesAgente(observacoesAgente);
        pedido.setDataAnalise(LocalDateTime.now());
        
        // Criar o aluguel efetivo
        Aluguel aluguel = Aluguel.builder()
            .cliente(pedido.getCliente())
            .carro(pedido.getCarro())
            .dataInicio(pedido.getDataInicio())
            .dataFim(pedido.getDataFim())
            .valorTotal(pedido.getValorTotal())
            .observacoes(pedido.getObservacoesCliente())
            .status(Aluguel.StatusAluguel.ATIVO)
            .build();
        
        aluguelService.criarAluguel(aluguel);
        
        // Atualizar status do carro
        Carro carro = pedido.getCarro();
        carro.setStatus(Carro.StatusCarro.ALUGADO);
        carroService.atualizar(carro);
        
        return pedidoAluguelRepository.save(pedido);
    }
    
    public PedidoAluguel negarPedido(Long pedidoId, Long agenteId, String observacoesAgente) {
        PedidoAluguel pedido = pedidoAluguelRepository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        
        if (pedido.getStatus() != PedidoAluguel.StatusPedido.PENDENTE) {
            throw new RuntimeException("Pedido já foi analisado");
        }
        
        Agente agente = agenteService.buscarPorId(agenteId)
            .orElseThrow(() -> new RuntimeException("Agente não encontrado"));
        
        pedido.setStatus(PedidoAluguel.StatusPedido.NEGADO);
        pedido.setAgente(agente);
        pedido.setObservacoesAgente(observacoesAgente);
        pedido.setDataAnalise(LocalDateTime.now());
        
        return pedidoAluguelRepository.save(pedido);
    }
    
    @Transactional(readOnly = true)
    public List<PedidoAluguel> listarTodos() {
        return pedidoAluguelRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<PedidoAluguel> buscarPorStatus(PedidoAluguel.StatusPedido status) {
        return pedidoAluguelRepository.findByStatusOrderByDataSolicitacaoDesc(status);
    }
    
    @Transactional(readOnly = true)
    public List<PedidoAluguel> buscarPorCliente(Long clienteId) {
        return pedidoAluguelRepository.findByClienteId(clienteId);
    }
    
    @Transactional(readOnly = true)
    public List<PedidoAluguel> buscarPorAgente(Long agenteId) {
        return pedidoAluguelRepository.findByAgenteId(agenteId);
    }
    
    @Transactional(readOnly = true)
    public Optional<PedidoAluguel> buscarPorId(Long id) {
        return pedidoAluguelRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<PedidoAluguel> buscarPorIdECliente(Long id, Long clienteId) {
        return pedidoAluguelRepository.findByIdAndClienteId(id, clienteId);
    }
}
