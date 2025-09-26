package com.aluguel_carros.aluguel_carros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "alugueis")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Aluguel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @NotNull(message = "O cliente é obrigatório")
    private Cliente cliente;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carro_id", nullable = false)
    @NotNull(message = "O carro é obrigatório")
    private Carro carro;
    
    @Column(name = "data_inicio", nullable = false)
    @NotNull(message = "A data de início é obrigatória")
    private LocalDateTime dataInicio;
    
    @Column(name = "data_fim")
    private LocalDateTime dataFim;
    
    @Column(name = "data_devolucao")
    private LocalDateTime dataDevolucao;
    
    @Positive(message = "O valor total deve ser positivo")
    @Column(name = "valor_total", precision = 10, scale = 2)
    private BigDecimal valorTotal;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAluguel status;
    
    @Column(name = "observacoes", length = 500)
    private String observacoes;
    
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;
    
    @PrePersist
    protected void onCreate() {
        dataCadastro = LocalDateTime.now();
        if (status == null) {
            status = StatusAluguel.ATIVO;
        }
    }
    
    public enum StatusAluguel {
        ATIVO,
        FINALIZADO,
        CANCELADO
    }
}
