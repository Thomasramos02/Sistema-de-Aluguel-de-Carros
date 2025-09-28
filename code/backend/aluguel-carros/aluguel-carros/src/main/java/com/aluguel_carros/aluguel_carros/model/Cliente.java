package com.aluguel_carros.aluguel_carros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clientes")
@Entity
public class Cliente extends Usuario {
	
    @NotBlank(message = "O telefone é obrigatório")
    @Column(nullable = false)
    private String telefone;

    @NotBlank(message = "O endereço é obrigatório")
    @Column(nullable = false)
    private String endereco;

    @NotBlank(message = "O RG é obrigatório")
    @Column(unique = true, nullable = false)
    private String rg;

    @NotBlank(message = "O CPF é obrigatório")
    @Column(unique = true, nullable = false)
    private String cpf;

    @PositiveOrZero(message = "Os rendimentos devem ser positivos ou zero")
    @Column(name = "rendimentos_auferidos")
    private Double rendimentosAuferidos;

    @NotBlank(message = "A profissão é obrigatória")
    @Column(nullable = false)
    private String profissao;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;
    
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Aluguel> alugueis;
    
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PedidoAluguel> pedidosAluguel;
    
    public static final int MAX_PROFISSAO = 3;
    
    @PrePersist
    protected void onCreate() {
        dataCadastro = LocalDateTime.now();
    }
}
