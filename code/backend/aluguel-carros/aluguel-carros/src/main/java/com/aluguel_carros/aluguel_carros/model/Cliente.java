package com.aluguel_carros.aluguel_carros.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clientes")
@Entity
public class Cliente extends Usuario {
	
    private String telefone;

    private String endereco;

    private String rg;

    private String CPF;

    private double rendimentosAuferidos;

    private String profissao;

    public static final int MAX_PROFISSAO = 3;

}
