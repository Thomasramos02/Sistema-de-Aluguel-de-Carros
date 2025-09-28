package com.aluguel_carros.aluguel_carros.services;

import com.aluguel_carros.aluguel_carros.model.Agente;
import com.aluguel_carros.aluguel_carros.repositories.AgenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AgenteService {
    
    private final AgenteRepository agenteRepository;
    private final PasswordEncoder passwordEncoder;
    
    public Agente salvar(Agente agente) {
        if (agenteRepository.existsByEmail(agente.getEmail())) {
            throw new RuntimeException("Email já cadastrado!");
        }
        
        if (agenteRepository.existsByCodigoAgente(agente.getCodigoAgente())) {
            throw new RuntimeException("Código do agente já cadastrado!");
        }
        
        agente.setSenha(passwordEncoder.encode(agente.getSenha()));
        return agenteRepository.save(agente);
    }
    
    public Agente atualizar(Agente agente) {
        Agente agenteExistente = agenteRepository.findById(agente.getId())
            .orElseThrow(() -> new RuntimeException("Agente não encontrado"));
        
        // Verificar se o email mudou e se já existe
        if (!agenteExistente.getEmail().equals(agente.getEmail()) && 
            agenteRepository.existsByEmail(agente.getEmail())) {
            throw new RuntimeException("Email já cadastrado!");
        }
        
        // Verificar se o código mudou e se já existe
        if (!agenteExistente.getCodigoAgente().equals(agente.getCodigoAgente()) && 
            agenteRepository.existsByCodigoAgente(agente.getCodigoAgente())) {
            throw new RuntimeException("Código do agente já cadastrado!");
        }
        
        // Se a senha foi alterada, criptografar
        if (agente.getSenha() != null && !agente.getSenha().isEmpty()) {
            agente.setSenha(passwordEncoder.encode(agente.getSenha()));
        } else {
            agente.setSenha(agenteExistente.getSenha());
        }
        
        return agenteRepository.save(agente);
    }
    
    @Transactional(readOnly = true)
    public List<Agente> listarTodos() {
        return agenteRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Agente> buscarPorId(Long id) {
        return agenteRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<Agente> buscarPorEmail(String email) {
        return agenteRepository.findByEmail(email);
    }
    
    @Transactional(readOnly = true)
    public Optional<Agente> buscarPorCodigoAgente(String codigoAgente) {
        return agenteRepository.findByCodigoAgente(codigoAgente);
    }
    
    public void excluir(Long id) {
        if (!agenteRepository.existsById(id)) {
            throw new RuntimeException("Agente não encontrado");
        }
        agenteRepository.deleteById(id);
    }
}
