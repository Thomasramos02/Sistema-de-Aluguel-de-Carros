package com.aluguel_carros.aluguel_carros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "carros")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Carro {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "A placa é obrigatória")
    @Column(unique = true, nullable = false)
    private String placa;
    
    @NotBlank(message = "O modelo é obrigatório")
    @Column(nullable = false)
    private String modelo;
    
    @NotBlank(message = "A marca é obrigatória")
    @Column(nullable = false)
    private String marca;
    
    @NotBlank(message = "A cor é obrigatória")
    @Column(nullable = false)
    private String cor;
    
    @NotNull(message = "O ano é obrigatório")
    @Column(nullable = false)
    private Integer ano;
    
    @NotNull(message = "O valor da diária é obrigatório")
    @Positive(message = "O valor da diária deve ser positivo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorDiaria;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCarro status;
    
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;
    
    @OneToMany(mappedBy = "carro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Aluguel> alugueis;
    
    @PrePersist
    protected void onCreate() {
        dataCadastro = LocalDateTime.now();
        if (status == null) {
            status = StatusCarro.DISPONIVEL;
        }
    }
    
    public enum StatusCarro {
        DISPONIVEL,
        ALUGADO,
        MANUTENCAO,
        INDISPONIVEL
    }
}
