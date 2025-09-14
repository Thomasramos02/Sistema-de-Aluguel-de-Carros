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

    public Cliente salvarCliente(Cliente cliente) {
        return clienteRepositories.saveAndFlush(cliente);
    }

    public void deletarCliente(Long id) {
        clienteRepositories.deleteById(id);
    }

    public Cliente buscarClientePorCPF(String CPF) {
        return clienteRepositories.findByCPF(CPF).orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com o CPF: " + CPF));
    }

  
       public void atualizarCliente(String CPF, Cliente cliente) {
    Cliente clienteAtual = buscarClientePorCPF(CPF);

 
    Cliente clienteAtualizado = Cliente.builder()
    .Id(clienteAtual.getId()) // <-- CORRETO: "id" minúsculo
    .nome(cliente.getNome() != null ? cliente.getNome() : clienteAtual.getNome())
    .email(cliente.getEmail() != null ? cliente.getEmail() : clienteAtual.getEmail())
    .build();

    clienteAtualizado.setSenha(
            cliente.getSenha() != null ? cliente.getSenha() : clienteAtual.getSenha()
    );
    clienteAtualizado.setTelefone(
            cliente.getTelefone() != null ? cliente.getTelefone() : clienteAtual.getTelefone()
    );
    clienteAtualizado.setEndereco(
            cliente.getEndereco() != null ? cliente.getEndereco() : clienteAtual.getEndereco()
    );
    clienteAtualizado.setRg(
            cliente.getRg() != null ? cliente.getRg() : clienteAtual.getRg()
    );
    clienteAtualizado.setCpf(
            cliente.getCpf() != null ? cliente.getCpf() : clienteAtual.getCpf()
    );
    clienteAtualizado.setRendimentosAuferidos(
            cliente.getRendimentosAuferidos() != 0 ? cliente.getRendimentosAuferidos() : clienteAtual.getRendimentosAuferidos()
    );
    clienteAtualizado.setProfissao(
            cliente.getProfissao() != null ? cliente.getProfissao() : clienteAtual.getProfissao()
    );



    clienteRepositories.save(clienteAtualizado);
}




}
