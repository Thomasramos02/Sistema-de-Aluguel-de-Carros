package com.aluguel_carros.aluguel_carros.controller;

import com.aluguel_carros.aluguel_carros.model.Aluguel;
import com.aluguel_carros.aluguel_carros.model.Carro;
import com.aluguel_carros.aluguel_carros.model.Cliente;
import com.aluguel_carros.aluguel_carros.services.AluguelService;
import com.aluguel_carros.aluguel_carros.services.CarroService;
import com.aluguel_carros.aluguel_carros.services.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/alugueis")
@RequiredArgsConstructor
public class AluguelController {
    
    private final AluguelService aluguelService;
    private final ClienteService clienteService;
    private final CarroService carroService;
    
    @GetMapping
    public String listarAlugueis(Model model, 
                                @RequestParam(required = false) String status) {
        List<Aluguel> alugueis;
        
        if (status != null && !status.trim().isEmpty()) {
            try {
                Aluguel.StatusAluguel statusAluguel = Aluguel.StatusAluguel.valueOf(status.toUpperCase());
                alugueis = aluguelService.buscarPorStatus(statusAluguel);
            } catch (IllegalArgumentException e) {
                alugueis = aluguelService.listarTodos();
            }
        } else {
            alugueis = aluguelService.listarTodos();
        }
        
        model.addAttribute("alugueis", alugueis);
        model.addAttribute("statusOptions", Aluguel.StatusAluguel.values());
        return "alugueis/lista";
    }
    
    @GetMapping("/novo")
    public String novoAluguel(Model model) {
        Aluguel aluguel = new Aluguel();
        aluguel.setDataInicio(LocalDateTime.now());
        
        model.addAttribute("aluguel", aluguel);
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("carrosDisponiveis", carroService.buscarDisponiveis());
        return "alugueis/form";
    }
    
    @PostMapping
    public String criarAluguel(@RequestParam Long clienteId,
                              @RequestParam Long carroId,
                              @RequestParam LocalDateTime dataInicio,
                              @RequestParam(required = false) LocalDateTime dataFim,
                              @RequestParam(required = false) String observacoes,
                              RedirectAttributes redirectAttributes) {
        
        try {
            // Buscar cliente e carro pelos IDs
            Cliente cliente = clienteService.buscarPorId(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
            
            Carro carro = carroService.buscarPorId(carroId)
                .orElseThrow(() -> new RuntimeException("Carro não encontrado"));
            
            // Criar objeto aluguel
            Aluguel aluguel = Aluguel.builder()
                .cliente(cliente)
                .carro(carro)
                .dataInicio(dataInicio)
                .dataFim(dataFim)
                .observacoes(observacoes)
                .build();
            
            aluguelService.criarAluguel(aluguel);
            redirectAttributes.addFlashAttribute("success", "Aluguel criado com sucesso!");
            return "redirect:/alugueis";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("clientes", clienteService.listarTodos());
            redirectAttributes.addFlashAttribute("carrosDisponiveis", carroService.buscarDisponiveis());
            return "redirect:/alugueis/novo";
        }
    }
    
    @GetMapping("/{id}")
    public String visualizarAluguel(@PathVariable Long id, Model model) {
        Aluguel aluguel = aluguelService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Aluguel não encontrado"));
        
        model.addAttribute("aluguel", aluguel);
        return "alugueis/detalhes";
    }
    
    @PostMapping("/{id}/finalizar")
    public String finalizarAluguel(@PathVariable Long id, 
                                   RedirectAttributes redirectAttributes) {
        try {
            aluguelService.finalizarAluguel(id, LocalDateTime.now());
            redirectAttributes.addFlashAttribute("success", "Aluguel finalizado com sucesso!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/alugueis";
    }
    
    @PostMapping("/{id}/cancelar")
    public String cancelarAluguel(@PathVariable Long id, 
                                  RedirectAttributes redirectAttributes) {
        try {
            aluguelService.cancelarAluguel(id);
            redirectAttributes.addFlashAttribute("success", "Aluguel cancelado com sucesso!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/alugueis";
    }
    
    @GetMapping("/cliente/{clienteId}")
    public String alugueisPorCliente(@PathVariable Long clienteId, Model model) {
        List<Aluguel> alugueis = aluguelService.buscarPorCliente(clienteId);
        Cliente cliente = clienteService.buscarPorId(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        model.addAttribute("alugueis", alugueis);
        model.addAttribute("cliente", cliente);
        return "alugueis/por-cliente";
    }
    
    @GetMapping("/carro/{carroId}")
    public String alugueisPorCarro(@PathVariable Long carroId, Model model) {
        List<Aluguel> alugueis = aluguelService.buscarPorCarro(carroId);
        Carro carro = carroService.buscarPorId(carroId)
            .orElseThrow(() -> new RuntimeException("Carro não encontrado"));
        
        model.addAttribute("alugueis", alugueis);
        model.addAttribute("carro", carro);
        return "alugueis/por-carro";
    }
}
