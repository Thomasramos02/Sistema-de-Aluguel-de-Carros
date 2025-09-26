package com.aluguel_carros.aluguel_carros.services;

import com.aluguel_carros.aluguel_carros.model.Carro;
import com.aluguel_carros.aluguel_carros.repositories.CarroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CarroService {
    
    private final CarroRepository carroRepository;
    
    public Carro salvar(Carro carro) {
        if (carroRepository.existsByPlaca(carro.getPlaca())) {
            throw new RuntimeException("Já existe um carro com esta placa: " + carro.getPlaca());
        }
        return carroRepository.save(carro);
    }
    
    public Carro atualizar(Carro carro) {
        Carro carroExistente = carroRepository.findById(carro.getId())
            .orElseThrow(() -> new RuntimeException("Carro não encontrado com ID: " + carro.getId()));
        
        // Verificar se a placa foi alterada e se já existe
        if (!carroExistente.getPlaca().equals(carro.getPlaca()) && 
            carroRepository.existsByPlaca(carro.getPlaca())) {
            throw new RuntimeException("Já existe um carro com esta placa: " + carro.getPlaca());
        }
        
        return carroRepository.save(carro);
    }
    
    public void deletar(Long id) {
        Carro carro = carroRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Carro não encontrado com ID: " + id));
        
        if (carro.getStatus() == Carro.StatusCarro.ALUGADO) {
            throw new RuntimeException("Não é possível deletar um carro que está alugado");
        }
        
        carroRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public List<Carro> listarTodos() {
        return carroRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Carro> buscarPorId(Long id) {
        return carroRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public List<Carro> buscarPorStatus(Carro.StatusCarro status) {
        return carroRepository.findByStatus(status);
    }
    
    @Transactional(readOnly = true)
    public List<Carro> buscarDisponiveis() {
        return carroRepository.findByStatus(Carro.StatusCarro.DISPONIVEL);
    }
    
    @Transactional(readOnly = true)
    public List<Carro> buscarPorMarca(String marca) {
        return carroRepository.findByMarca(marca);
    }
    
    @Transactional(readOnly = true)
    public List<Carro> buscarPorModelo(String modelo) {
        return carroRepository.findByModelo(modelo);
    }
    
    @Transactional(readOnly = true)
    public List<Carro> buscarDisponiveisPorValorMaximo(BigDecimal valorMaximo) {
        return carroRepository.findDisponiveisPorValorMaximo(Carro.StatusCarro.DISPONIVEL, valorMaximo);
    }
    
    @Transactional(readOnly = true)
    public List<Carro> buscarDisponiveisPorTermo(String termo) {
        return carroRepository.buscarDisponiveisPorTermo(termo);
    }
    
    public Carro alterarStatus(Long id, Carro.StatusCarro novoStatus) {
        Carro carro = carroRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Carro não encontrado com ID: " + id));
        
        carro.setStatus(novoStatus);
        return carroRepository.save(carro);
    }
}
