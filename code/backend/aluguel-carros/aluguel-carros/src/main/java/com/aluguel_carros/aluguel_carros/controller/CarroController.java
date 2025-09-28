package com.aluguel_carros.aluguel_carros.controller;

import com.aluguel_carros.aluguel_carros.model.Carro;
import com.aluguel_carros.aluguel_carros.services.CarroService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/carros")
@RequiredArgsConstructor
public class CarroController {
    
    private final CarroService carroService;
    
    @GetMapping
    @PreAuthorize("hasRole('FUNCIONARIO') or hasRole('ADMIN')")
    public String listarCarros(Model model, 
                              @RequestParam(required = false) String busca,
                              @RequestParam(required = false) String status) {
        List<Carro> carros;
        
        if (busca != null && !busca.trim().isEmpty()) {
            carros = carroService.buscarDisponiveisPorTermo(busca);
        } else if (status != null && !status.trim().isEmpty()) {
            try {
                Carro.StatusCarro statusCarro = Carro.StatusCarro.valueOf(status.toUpperCase());
                carros = carroService.buscarPorStatus(statusCarro);
            } catch (IllegalArgumentException e) {
                carros = carroService.listarTodos();
            }
        } else {
            carros = carroService.listarTodos();
        }
        
        model.addAttribute("carros", carros);
        model.addAttribute("statusOptions", Carro.StatusCarro.values());
        return "carros/lista";
    }
    
    @GetMapping("/novo")
    @PreAuthorize("hasRole('FUNCIONARIO') or hasRole('ADMIN')")
    public String novoCarro(Model model) {
        model.addAttribute("carro", new Carro());
        return "carros/form";
    }
    
    @PostMapping
    @PreAuthorize("hasRole('FUNCIONARIO') or hasRole('ADMIN')")
    public String salvarCarro(@Valid @ModelAttribute Carro carro, 
                             BindingResult result, 
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "carros/form";
        }
        
        try {
            carroService.salvar(carro);
            redirectAttributes.addFlashAttribute("success", "Carro cadastrado com sucesso!");
            return "redirect:/carros";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/carros/novo";
        }
    }
    
    @GetMapping("/{id}/editar")
    @PreAuthorize("hasRole('FUNCIONARIO') or hasRole('ADMIN')")
    public String editarCarro(@PathVariable Long id, Model model) {
        Carro carro = carroService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Carro não encontrado"));
        
        model.addAttribute("carro", carro);
        return "carros/form";
    }
    
    @PostMapping("/{id}")
    @PreAuthorize("hasRole('FUNCIONARIO') or hasRole('ADMIN')")
    public String atualizarCarro(@PathVariable Long id, 
                                @Valid @ModelAttribute Carro carro, 
                                BindingResult result, 
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "carros/form";
        }
        
        try {
            carro.setId(id);
            carroService.atualizar(carro);
            redirectAttributes.addFlashAttribute("success", "Carro atualizado com sucesso!");
            return "redirect:/carros";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/carros/" + id + "/editar";
        }
    }
    
    @PostMapping("/{id}/deletar")
    @PreAuthorize("hasRole('FUNCIONARIO') or hasRole('ADMIN')")
    public String deletarCarro(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            carroService.deletar(id);
            redirectAttributes.addFlashAttribute("success", "Carro deletado com sucesso!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/carros";
    }
    
    @PostMapping("/{id}/status")
    @PreAuthorize("hasRole('FUNCIONARIO') or hasRole('ADMIN')")
    public String alterarStatus(@PathVariable Long id, 
                               @RequestParam Carro.StatusCarro status, 
                               RedirectAttributes redirectAttributes) {
        try {
            carroService.alterarStatus(id, status);
            redirectAttributes.addFlashAttribute("success", "Status do carro alterado com sucesso!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/carros";
    }
    
    @GetMapping("/disponiveis")
    public String carrosDisponiveis(Model model) {
        List<Carro> carros = carroService.buscarDisponiveis();
        model.addAttribute("carros", carros);
        return "carros/disponiveis";
    }
    
    @GetMapping("/visualizar")
    public String visualizarCarros(Model model) {
        List<Carro> carros = carroService.buscarDisponiveis();
        model.addAttribute("carros", carros);
        return "carros/visualizar";
    }
}
