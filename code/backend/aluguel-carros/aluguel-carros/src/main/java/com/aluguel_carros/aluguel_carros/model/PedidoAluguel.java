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
@Table(name = "pedidos_aluguel")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoAluguel {
    //juntar essa classe PedidoAluguel com Aluguel assim teria um modelo de dados mais simples
    // e a classe estaria mais limpa, o banco de dados n precisaria criar outra tabela tbm
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
    
    @Column(name = "data_fim", nullable = false)
    @NotNull(message = "A data de fim é obrigatória")
    private LocalDateTime dataFim;
    
    @Positive(message = "O valor total deve ser positivo")
    @Column(name = "valor_total", precision = 10, scale = 2)
    private BigDecimal valorTotal;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agente_id")
    private Agente agente;
    
    @Column(name = "observacoes_cliente", length = 500)
    private String observacoesCliente;
    
    @Column(name = "observacoes_agente", length = 500)
    private String observacoesAgente;
    
    @Column(name = "data_solicitacao")
    private LocalDateTime dataSolicitacao;
    
    @Column(name = "data_analise")
    private LocalDateTime dataAnalise;
    
    @PrePersist
    protected void onCreate() {
        dataSolicitacao = LocalDateTime.now();
        if (status == null) {
            status = StatusPedido.PENDENTE;
        }
    }
    
    public enum StatusPedido {
        PENDENTE,
        APROVADO,
        NEGADO
    }
}
