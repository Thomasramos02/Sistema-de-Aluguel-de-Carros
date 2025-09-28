package com.aluguel_carros.aluguel_carros.controller;

import com.aluguel_carros.aluguel_carros.model.Aluguel;
import com.aluguel_carros.aluguel_carros.model.Carro;
import com.aluguel_carros.aluguel_carros.model.Cliente;
import com.aluguel_carros.aluguel_carros.services.AluguelService;
import com.aluguel_carros.aluguel_carros.services.CarroService;
import com.aluguel_carros.aluguel_carros.services.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
@Slf4j
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
        try {
            log.info("Iniciando criação de novo aluguel...");
            
            // Criar aluguel básico
            Aluguel aluguel = new Aluguel();
            aluguel.setDataInicio(LocalDateTime.now());
            model.addAttribute("aluguel", aluguel);
            log.info("Aluguel criado com sucesso");
            
            // Buscar carros disponíveis com tratamento de erro
            try {
                log.info("Buscando carros disponíveis...");
                List<Carro> carrosDisponiveis = carroService.buscarDisponiveis();
                log.info("Encontrados {} carros disponíveis", carrosDisponiveis.size());
                model.addAttribute("carrosDisponiveis", carrosDisponiveis);
            } catch (Exception e) {
                log.error("Erro ao buscar carros disponíveis", e);
                model.addAttribute("carrosDisponiveis", List.of());
                model.addAttribute("error", "Erro ao carregar carros: " + e.getMessage());
            }
            
            log.info("Retornando template alugueis/form-simple");
            return "alugueis/form-simple";
            
        } catch (Exception e) {
            log.error("Erro crítico ao criar novo aluguel", e);
            model.addAttribute("error", "Erro interno: " + e.getMessage());
            return "error";
        }
    }
    
   @PostMapping
public String criarAluguel(@RequestParam Long carroId,
                            @RequestParam String dataInicio,
                            @RequestParam String dataFim,
                            @RequestParam(required = false) String observacoes,
                            RedirectAttributes redirectAttributes) {

    try {
        log.info("Iniciando criação de aluguel...");
        
        // Obter cliente logado automaticamente
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Cliente cliente = clienteService.buscarPorEmail(email)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado. Faça login primeiro."));

        Carro carro = carroService.buscarPorId(carroId)
            .orElseThrow(() -> new RuntimeException("Carro não encontrado"));

        // Converter strings para LocalDateTime
        LocalDateTime inicio = LocalDateTime.parse(dataInicio);
        LocalDateTime fim = LocalDateTime.parse(dataFim);

        // Criar aluguel com todos os campos necessários
        Aluguel aluguel = Aluguel.builder()
            .cliente(cliente)
            .carro(carro)
            .dataInicio(inicio)
            .dataFim(fim)
            .observacoes(observacoes)
            .status(Aluguel.StatusAluguel.ATIVO)
            .build();

        log.info("Criando aluguel para cliente: {} e carro: {}", cliente.getNome(), carro.getPlaca());
        aluguelService.criarAluguel(aluguel);

        redirectAttributes.addFlashAttribute("success", "Aluguel criado com sucesso!");
        return "redirect:/alugueis";

    } catch (RuntimeException e) {
        log.error("Erro ao criar aluguel", e);
        redirectAttributes.addFlashAttribute("error", e.getMessage());
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
    @PreAuthorize("hasRole('FUNCIONARIO') or hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('FUNCIONARIO') or hasRole('ADMIN')")
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
