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
        Cliente clienteSalvo = clienteServices.salvarCliente(cliente);
        return ResponseEntity.ok(clienteSalvo);
    }

   @GetMapping("/buscarCliente")
   public ResponseEntity<Cliente> buscarCliente(@RequestParam String CPF) {
       Cliente cliente = clienteServices.buscarClientePorCPF(CPF);
       return ResponseEntity.ok(cliente);
   }

   @PutMapping("/atualizarCliente")
   public ResponseEntity<Cliente> atualizarCliente(@RequestParam String CPF, @RequestBody Cliente cliente) {
       clienteServices.atualizarCliente(CPF, cliente);
       return ResponseEntity.ok().build();
   }

   @DeleteMapping("/deletarCliente")
   public ResponseEntity<Void> deletarCliente(@RequestParam Long id) {
       clienteServices.deletarCliente(id);
       return ResponseEntity.noContent().build();
   }

}
