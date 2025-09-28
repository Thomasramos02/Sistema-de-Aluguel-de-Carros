package com.aluguel_carros.aluguel_carros.services;

import com.aluguel_carros.aluguel_carros.model.UsuarioAuth;
import com.aluguel_carros.aluguel_carros.repositories.UsuarioAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioAuthService {
    
    private final UsuarioAuthRepository usuarioAuthRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UsuarioAuth salvar(UsuarioAuth usuario) {
        if (usuarioAuthRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Já existe um usuário com este email: " + usuario.getEmail());
        }
        
        // Criptografar senha
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        
        return usuarioAuthRepository.save(usuario);
    }
    
    public UsuarioAuth atualizar(UsuarioAuth usuario) {
        UsuarioAuth usuarioExistente = usuarioAuthRepository.findById(usuario.getId())
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        // Verificar se email foi alterado
        if (!usuarioExistente.getEmail().equals(usuario.getEmail()) && 
            usuarioAuthRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Já existe um usuário com este email: " + usuario.getEmail());
        }
        
        // Se a senha foi alterada, criptografar
        if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        } else {
            usuario.setSenha(usuarioExistente.getSenha());
        }
        
        return usuarioAuthRepository.save(usuario);
    }
    
    public void deletar(Long id) {
        UsuarioAuth usuario = usuarioAuthRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        usuario.setAtivo(false);
        usuarioAuthRepository.save(usuario);
    }
    
    @Transactional(readOnly = true)
    public List<UsuarioAuth> listarTodos() {
        return usuarioAuthRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<UsuarioAuth> buscarPorId(Long id) {
        return usuarioAuthRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<UsuarioAuth> buscarPorEmail(String email) {
        return usuarioAuthRepository.findByEmail(email);
    }
}
