package com.aluguel_carros.aluguel_carros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios_auth")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioAuth implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "O nome é obrigatório")
    @Column(nullable = false)
    private String nome;
    
    @Email(message = "Email deve ter formato válido")
    @NotBlank(message = "O email é obrigatório")
    @Column(unique = true, nullable = false)
    private String email;
    
    @NotBlank(message = "A senha é obrigatória")
    @Column(nullable = false)
    private String senha;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;
    
    @Column(name = "ativo")
    private Boolean ativo;
    
    @PrePersist
    protected void onCreate() {
        dataCadastro = LocalDateTime.now();
        if (ativo == null) {
            ativo = true;
        }
        if (role == null) {
            role = Role.CLIENTE;
        }
    }
    
    // Implementação do UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
    
    @Override
    public String getPassword() {
        return senha;
    }
    
    @Override
    public String getUsername() {
        return email;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return ativo;
    }
    
    public enum Role {
        ADMIN,
        CLIENTE,
        FUNCIONARIO
    }
}
