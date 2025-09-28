package com.aluguel_carros.aluguel_carros.config;

import com.aluguel_carros.aluguel_carros.model.Agente;
import com.aluguel_carros.aluguel_carros.model.Cliente;
import com.aluguel_carros.aluguel_carros.services.AgenteService;
import com.aluguel_carros.aluguel_carros.services.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Primary
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    
    private final ClienteService clienteService;
    private final AgenteService agenteService;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Tentando fazer login com email: {}", email);
        
        // Tentar buscar como cliente primeiro
        Cliente cliente = clienteService.buscarPorEmail(email).orElse(null);
        if (cliente != null) {
            log.info("Cliente encontrado: {}", cliente.getNome());
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_CLIENTE"));
            
            return User.builder()
                    .username(cliente.getEmail())
                    .password(cliente.getSenha())
                    .authorities(authorities)
                    .build();
        }
        
        // Se não for cliente, tentar como agente
        Agente agente = agenteService.buscarPorEmail(email).orElse(null);
        if (agente != null) {
            log.info("Agente encontrado: {}", agente.getNome());
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_FUNCIONARIO"));
            
            return User.builder()
                    .username(agente.getEmail())
                    .password(agente.getSenha())
                    .authorities(authorities)
                    .build();
        }
        
        log.error("Usuário não encontrado: {}", email);
        throw new UsernameNotFoundException("Usuário não encontrado: " + email);
    }
}
