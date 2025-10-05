package com.aluguel_carros.aluguel_carros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

/*
 * Refatoração do Agente.java para herdar de Usuario
 * - Remove campos duplicados (id, nome, email, senha, dataCadastro)
 * - Mantém campos específicos de Agente (codigoAgente, departamento, dataAdmissao, ativo)
 * - Mantém relação com pedidos analisados
 * - onCreate agora trata apenas campos específicos do Agente
 */
//tudo isso para usar boas praticas como heranca e deixar codigo da model mais limpo
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "agentes")
@Entity
public class Agente extends Usuario {

    @NotBlank(message = "O código do agente é obrigatório")
    @Column(unique = true, nullable = false, name = "codigo_agente")
    private String codigoAgente;

    @NotBlank(message = "O departamento é obrigatório")
    @Column(nullable = false)
    private String departamento;

    @Column(name = "data_admissao")
    private LocalDateTime dataAdmissao;

    @Column(name = "ativo")
    private Boolean ativo;

    @OneToMany(mappedBy = "agente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PedidoAluguel> pedidosAnalisados;

    @PrePersist
    protected void onCreate() {
        // Apenas inicializa campos específicos do Agente
        if (dataAdmissao == null) {
            dataAdmissao = LocalDateTime.now();
        }
        if (ativo == null) {
            ativo = true;
        }
    }
}
