package com.aluguel_carros.aluguel_carros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "agentes")
@Entity
public class Agente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "O nome é obrigatório")
    @Column(nullable = false)
    private String nome;
    
    @NotBlank(message = "O email é obrigatório")
    @Column(unique = true, nullable = false)
    private String email;
    
    @NotBlank(message = "A senha é obrigatória")
    @Column(nullable = false)
    private String senha;
    
    @NotBlank(message = "O código do agente é obrigatório")
    @Column(unique = true, nullable = false, name = "codigo_agente")
    private String codigoAgente;
    
    @NotBlank(message = "O departamento é obrigatório")
    @Column(nullable = false)
    private String departamento;
    
    @Column(name = "data_admissao")
    private LocalDateTime dataAdmissao;
    
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;
    
    @Column(name = "ativo")
    private Boolean ativo;
    
    @OneToMany(mappedBy = "agente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PedidoAluguel> pedidosAnalisados;
    
    @PrePersist
    protected void onCreate() {
        dataCadastro = LocalDateTime.now();
        dataAdmissao = LocalDateTime.now();
        if (ativo == null) {
            ativo = true;
        }
    }
}
