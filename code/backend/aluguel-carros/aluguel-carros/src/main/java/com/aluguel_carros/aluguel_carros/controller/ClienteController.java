package com.aluguel_carros.aluguel_carros.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aluguel_carros.aluguel_carros.model.Cliente;
import com.aluguel_carros.aluguel_carros.services.ClienteServices;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteServices clienteServices;

    @PostMapping("/criarCliente")
    public ResponseEntity<Cliente> criarCliente(@RequestBody Cliente cliente) {
        
        clienteServices.salvarCliente(cliente);

        return ResponseEntity.status(201).body(cliente);
    }



    @GetMapping("/buscarCliente")
    public ResponseEntity<Cliente> buscarCliente(@RequestParam String cpf) {
        Cliente cliente = clienteServices.buscarPorCPF(cpf);
        return ResponseEntity.ok(cliente);
    }

    @DeleteMapping("/deletarCliente")
    public ResponseEntity<Void> deletarCliente(@RequestParam String cpf) {
        clienteServices.deletarPorCPF(cpf);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/atualizarCliente")
    public ResponseEntity<Void> atualizarCliente(@RequestParam String cpf, @RequestBody Cliente cliente) {
        clienteServices.atualizarCliente(cpf, cliente);
        return ResponseEntity.noContent().build();
    }
 
}
