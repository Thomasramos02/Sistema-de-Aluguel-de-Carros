package com.aluguel_carros.aluguel_carros.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aluguel_carros.aluguel_carros.model.Cliente;
import com.aluguel_carros.aluguel_carros.repositories.ClienteRepositories;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ClienteServices {
    @Autowired
    private ClienteRepositories clienteRepositories;

    public Cliente buscarPorCPF(String CPF) {
        return clienteRepositories.findByCPF(CPF)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
    }

    public void deletarPorCPF(String CPF) {
        clienteRepositories.deleteByCPF(CPF);
    }

    public Cliente salvarCliente(Cliente cliente) {
        return clienteRepositories.saveAndFlush(cliente);
    }

    public void atualizarCliente(String cpf, Cliente cliente) {
        Cliente clienteAtual = buscarPorCPF(cpf);
        Cliente clienteAtualizado = Cliente.builder()
        .id(clienteAtual.getId())
        .nome(cliente.getNome() != null ? cliente.getNome() : clienteAtual.getNome())
        .email(cliente.getEmail() != null ? cliente.getEmail() : clienteAtual.getEmail())
        .senha(cliente.getSenha() != null ? cliente.getSenha() : clienteAtual.getSenha())
        .telefone(cliente.getTelefone() != null ? cliente.getTelefone() : clienteAtual.getTelefone())
        .endereco(cliente.getEndereco() != null ? cliente.getEndereco() : clienteAtual.getEndereco())
        .rg(cliente.getRg() != null ? cliente.getRg() : clienteAtual.getRg())
        .CPF(cliente.getCPF() != null ? cliente.getCPF() : clienteAtual.getCPF())
        .rendimentosAuferidos(cliente.getRendimentosAuferidos() != 0 ? cliente.getRendimentosAuferidos() : clienteAtual.getRendimentosAuferidos())
        .profissao(cliente.getProfissao() != null ? cliente.getProfissao() : clienteAtual.getProfissao())
        .build();
        clienteRepositories.saveAndFlush(clienteAtualizado);

    }

}
