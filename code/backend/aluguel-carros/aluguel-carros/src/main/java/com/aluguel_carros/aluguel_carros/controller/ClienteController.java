package com.aluguel_carros.aluguel_carros.controller;

import com.aluguel_carros.aluguel_carros.model.Cliente;
import com.aluguel_carros.aluguel_carros.services.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {
    
    private final ClienteService clienteService;
    
    @GetMapping
    @PreAuthorize("hasRole('FUNCIONARIO') or hasRole('ADMIN')")
    public String listarClientes(Model model, 
                                @RequestParam(required = false) String busca) {
        List<Cliente> clientes;
        
        if (busca != null && !busca.trim().isEmpty()) {
            clientes = clienteService.buscarPorNome(busca);
        } else {
            clientes = clienteService.listarTodos();
        }
        
        model.addAttribute("clientes", clientes);
        return "clientes/lista";
    }
    
    @GetMapping("/novo")
    @PreAuthorize("hasRole('FUNCIONARIO') or hasRole('ADMIN')")
    public String novoCliente(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes/form";
    }
    
    @PostMapping
    @PreAuthorize("hasRole('FUNCIONARIO') or hasRole('ADMIN')")
    public String salvarCliente(@Valid @ModelAttribute Cliente cliente, 
                               BindingResult result, 
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "clientes/form";
        }
        
        try {
            clienteService.salvar(cliente);
            redirectAttributes.addFlashAttribute("success", "Cliente cadastrado com sucesso!");
            return "redirect:/clientes";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/clientes/novo";
        }
    }
    
    @GetMapping("/{id}/editar")
    @PreAuthorize("hasRole('FUNCIONARIO') or hasRole('ADMIN')")
    public String editarCliente(@PathVariable Long id, Model model) {
        Cliente cliente = clienteService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        model.addAttribute("cliente", cliente);
        return "clientes/form";
    }
    
    @PostMapping("/{id}")
    @PreAuthorize("hasRole('FUNCIONARIO') or hasRole('ADMIN')")
    public String atualizarCliente(@PathVariable Long id, 
                                  @Valid @ModelAttribute Cliente cliente, 
                                  BindingResult result, 
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "clientes/form";
        }
        
        try {
            cliente.setId(id);
            clienteService.atualizar(cliente);
            redirectAttributes.addFlashAttribute("success", "Cliente atualizado com sucesso!");
            return "redirect:/clientes";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/clientes/" + id + "/editar";
        }
    }
    
    @PostMapping("/{id}/deletar")
    public String deletarCliente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            clienteService.deletar(id);
            redirectAttributes.addFlashAttribute("success", "Cliente deletado com sucesso!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/clientes";
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('FUNCIONARIO') or hasRole('ADMIN')")
    public String visualizarCliente(@PathVariable Long id, Model model) {
        Cliente cliente = clienteService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        model.addAttribute("cliente", cliente);
        return "clientes/detalhes";
    }
    
    @GetMapping("/buscar")
    @PreAuthorize("hasRole('FUNCIONARIO') or hasRole('ADMIN')")
    public String buscarClientes(@RequestParam String termo, Model model) {
        List<Cliente> clientes = clienteService.buscarPorNome(termo);
        model.addAttribute("clientes", clientes);
        model.addAttribute("termoBusca", termo);
        return "clientes/lista";
    }
}