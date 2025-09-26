package com.aluguel_carros.aluguel_carros.services;

import com.aluguel_carros.aluguel_carros.model.Aluguel;
import com.aluguel_carros.aluguel_carros.model.Carro;
import com.aluguel_carros.aluguel_carros.model.Cliente;
import com.aluguel_carros.aluguel_carros.repositories.AluguelRepository;
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
public class AluguelService {
    
    private final AluguelRepository aluguelRepository;
    private final CarroService carroService;
    private final ClienteService clienteService;
    
    public Aluguel criarAluguel(Aluguel aluguel) {
        // Validar se o cliente existe
        Cliente cliente = clienteService.buscarPorId(aluguel.getCliente().getId())
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        // Validar se o carro existe e está disponível
        Carro carro = carroService.buscarPorId(aluguel.getCarro().getId())
            .orElseThrow(() -> new RuntimeException("Carro não encontrado"));
        
        if (carro.getStatus() != Carro.StatusCarro.DISPONIVEL) {
            throw new RuntimeException("Carro não está disponível para aluguel");
        }
        
        // Verificar conflitos de datas
        List<Aluguel> conflitos = aluguelRepository.findConflitosPorCarro(
            carro.getId(), aluguel.getDataInicio(), aluguel.getDataFim());
        
        if (!conflitos.isEmpty()) {
            throw new RuntimeException("Carro já está alugado neste período");
        }
        
        // Calcular valor total
        if (aluguel.getDataFim() != null) {
            long dias = ChronoUnit.DAYS.between(aluguel.getDataInicio(), aluguel.getDataFim());
            BigDecimal valorTotal = carro.getValorDiaria().multiply(BigDecimal.valueOf(dias));
            aluguel.setValorTotal(valorTotal);
        }
        
        // Alterar status do carro para ALUGADO
        carroService.alterarStatus(carro.getId(), Carro.StatusCarro.ALUGADO);
        
        return aluguelRepository.save(aluguel);
    }
    
    public Aluguel finalizarAluguel(Long id, LocalDateTime dataDevolucao) {
        Aluguel aluguel = aluguelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Aluguel não encontrado com ID: " + id));
        
        if (aluguel.getStatus() != Aluguel.StatusAluguel.ATIVO) {
            throw new RuntimeException("Apenas aluguéis ativos podem ser finalizados");
        }
        
        aluguel.setDataDevolucao(dataDevolucao);
        aluguel.setStatus(Aluguel.StatusAluguel.FINALIZADO);
        
        // Alterar status do carro para DISPONIVEL
        carroService.alterarStatus(aluguel.getCarro().getId(), Carro.StatusCarro.DISPONIVEL);
        
        return aluguelRepository.save(aluguel);
    }
    
    public Aluguel cancelarAluguel(Long id) {
        Aluguel aluguel = aluguelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Aluguel não encontrado com ID: " + id));
        
        if (aluguel.getStatus() != Aluguel.StatusAluguel.ATIVO) {
            throw new RuntimeException("Apenas aluguéis ativos podem ser cancelados");
        }
        
        aluguel.setStatus(Aluguel.StatusAluguel.CANCELADO);
        
        // Alterar status do carro para DISPONIVEL
        carroService.alterarStatus(aluguel.getCarro().getId(), Carro.StatusCarro.DISPONIVEL);
        
        return aluguelRepository.save(aluguel);
    }
    
    @Transactional(readOnly = true)
    public List<Aluguel> listarTodos() {
        return aluguelRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Aluguel> buscarPorId(Long id) {
        return aluguelRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public List<Aluguel> buscarPorCliente(Long clienteId) {
        return aluguelRepository.findByClienteId(clienteId);
    }
    
    @Transactional(readOnly = true)
    public List<Aluguel> buscarPorCarro(Long carroId) {
        return aluguelRepository.findByCarroId(carroId);
    }
    
    @Transactional(readOnly = true)
    public List<Aluguel> buscarPorStatus(Aluguel.StatusAluguel status) {
        return aluguelRepository.findByStatus(status);
    }
    
    @Transactional(readOnly = true)
    public List<Aluguel> buscarAlugueisAtivosPorCliente(Long clienteId) {
        return aluguelRepository.findByClienteIdAndStatus(clienteId, Aluguel.StatusAluguel.ATIVO);
    }
    
    @Transactional(readOnly = true)
    public Long contarAlugueisAtivosPorCliente(Long clienteId) {
        return aluguelRepository.countAlugueisAtivosPorCliente(clienteId);
    }
    
    @Transactional(readOnly = true)
    public List<Aluguel> buscarPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        return aluguelRepository.findByPeriodo(dataInicio, dataFim);
    }
}
