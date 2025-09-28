package com.aluguel_carros.aluguel_carros.services;

import com.aluguel_carros.aluguel_carros.model.Cliente;
import com.aluguel_carros.aluguel_carros.repositories.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteService {
    
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    
    public Cliente salvar(Cliente cliente) {
        // Validar unicidade de email, CPF e RG
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new RuntimeException("Já existe um cliente com este email: " + cliente.getEmail());
        }
        
        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new RuntimeException("Já existe um cliente com este CPF: " + cliente.getCpf());
        }
        
        if (clienteRepository.existsByRg(cliente.getRg())) {
            throw new RuntimeException("Já existe um cliente com este RG: " + cliente.getRg());
        }
        
        // Criptografar senha
        cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));
        
        return clienteRepository.save(cliente);
    }
    
    public Cliente atualizar(Cliente cliente) {
        Cliente clienteExistente = clienteRepository.findById(cliente.getId())
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + cliente.getId()));
        
        // Verificar se email foi alterado e se já existe
        if (!clienteExistente.getEmail().equals(cliente.getEmail()) && 
            clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new RuntimeException("Já existe um cliente com este email: " + cliente.getEmail());
        }
        
        // Verificar se CPF foi alterado e se já existe
        if (!clienteExistente.getCpf().equals(cliente.getCpf()) && 
            clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new RuntimeException("Já existe um cliente com este CPF: " + cliente.getCpf());
        }
        
        // Verificar se RG foi alterado e se já existe
        if (!clienteExistente.getRg().equals(cliente.getRg()) && 
            clienteRepository.existsByRg(cliente.getRg())) {
            throw new RuntimeException("Já existe um cliente com este RG: " + cliente.getRg());
        }
        
        // Se a senha foi alterada, criptografar
        if (cliente.getSenha() != null && !cliente.getSenha().isEmpty()) {
            cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));
        } else {
            cliente.setSenha(clienteExistente.getSenha());
        }
        
        return clienteRepository.save(cliente);
    }
    
    public void deletar(Long id) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + id));
        
        clienteRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }
    
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorCpf(String cpf) {
        return clienteRepository.findByCpf(cpf);
    }
    
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorRg(String rg) {
        return clienteRepository.findByRg(rg);
    }
    
    @Transactional(readOnly = true)
    public List<Cliente> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }
    
    @Transactional(readOnly = true)
    public List<Cliente> buscarPorProfissao(String profissao) {
        return clienteRepository.findByProfissaoContainingIgnoreCase(profissao);
    }
}
