package com.aluguel_carros.aluguel_carros.controller;

import com.aluguel_carros.aluguel_carros.model.Agente;
import com.aluguel_carros.aluguel_carros.model.PedidoAluguel;
import com.aluguel_carros.aluguel_carros.services.AgenteService;
import com.aluguel_carros.aluguel_carros.services.PedidoAluguelService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/agentes")
@RequiredArgsConstructor
public class AgenteController {
    
    private final AgenteService agenteService;
    private final PedidoAluguelService pedidoAluguelService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String listarAgentes(Model model) {
        List<Agente> agentes = agenteService.listarTodos();
        model.addAttribute("agentes", agentes);
        return "agentes/lista";
    }
    
    @GetMapping("/novo")
    @PreAuthorize("hasRole('ADMIN')")
    public String novoAgente(Model model) {
        model.addAttribute("agente", new Agente());
        return "agentes/form";
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String criarAgente(@Valid @ModelAttribute Agente agente,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "agentes/form";
        }
        
        try {
            agenteService.salvar(agente);
            redirectAttributes.addFlashAttribute("success", "Agente criado com sucesso!");
            return "redirect:/agentes";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/agentes/novo";
        }
    }
    
    @GetMapping("/pedidos")
    public String listarPedidos(Model model,
                               @RequestParam(required = false) String status) {
        List<PedidoAluguel> pedidos;
        
        if (status != null && !status.trim().isEmpty()) {
            try {
                PedidoAluguel.StatusPedido statusPedido = PedidoAluguel.StatusPedido.valueOf(status.toUpperCase());
                pedidos = pedidoAluguelService.buscarPorStatus(statusPedido);
            } catch (IllegalArgumentException e) {
                pedidos = pedidoAluguelService.listarTodos();
            }
        } else {
            pedidos = pedidoAluguelService.listarTodos();
        }
        
        // Obter ID do agente logado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Long agenteId = agenteService.buscarPorEmail(email)
            .map(Agente::getId)
            .orElse(null); // Pode ser admin sem ser agente
        
        model.addAttribute("pedidos", pedidos);
        model.addAttribute("statusOptions", PedidoAluguel.StatusPedido.values());
        model.addAttribute("agenteId", agenteId);
        return "agentes/pedidos";
    }
    
    @GetMapping("/pedidos/{id}")
    public String visualizarPedido(@PathVariable Long id, Model model) {
        PedidoAluguel pedido = pedidoAluguelService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        
        // Obter ID do agente logado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Long agenteId = agenteService.buscarPorEmail(email)
            .map(Agente::getId)
            .orElse(null); // Pode ser admin sem ser agente
        
        model.addAttribute("pedido", pedido);
        model.addAttribute("agenteId", agenteId);
        return "agentes/detalhes-pedido";
    }
    
    @PostMapping("/pedidos/{id}/aprovar")
    public String aprovarPedido(@PathVariable Long id,
                               @RequestParam String observacoesAgente,
                               @RequestParam Long agenteId,
                               RedirectAttributes redirectAttributes) {
        try {
            pedidoAluguelService.aprovarPedido(id, agenteId, observacoesAgente);
            redirectAttributes.addFlashAttribute("success", "Pedido aprovado com sucesso!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/agentes/pedidos";
    }
    
    @PostMapping("/pedidos/{id}/negar")
    public String negarPedido(@PathVariable Long id,
                             @RequestParam String observacoesAgente,
                             @RequestParam Long agenteId,
                             RedirectAttributes redirectAttributes) {
        try {
            pedidoAluguelService.negarPedido(id, agenteId, observacoesAgente);
            redirectAttributes.addFlashAttribute("success", "Pedido negado com sucesso!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/agentes/pedidos";
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String visualizarAgente(@PathVariable Long id, Model model) {
        Agente agente = agenteService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Agente não encontrado"));
        
        model.addAttribute("agente", agente);
        return "agentes/detalhes";
    }
    
    @GetMapping("/{id}/editar")
    @PreAuthorize("hasRole('ADMIN')")
    public String editarAgente(@PathVariable Long id, Model model) {
        Agente agente = agenteService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Agente não encontrado"));
        
        model.addAttribute("agente", agente);
        return "agentes/form";
    }
    
    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String atualizarAgente(@PathVariable Long id,
                                 @Valid @ModelAttribute Agente agente,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "agentes/form";
        }
        
        try {
            agente.setId(id);
            agenteService.atualizar(agente);
            redirectAttributes.addFlashAttribute("success", "Agente atualizado com sucesso!");
            return "redirect:/agentes";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/agentes/" + id + "/editar";
        }
    }
    
    @PostMapping("/{id}/excluir")
    @PreAuthorize("hasRole('ADMIN')")
    public String excluirAgente(@PathVariable Long id,
                               RedirectAttributes redirectAttributes) {
        try {
            agenteService.excluir(id);
            redirectAttributes.addFlashAttribute("success", "Agente excluído com sucesso!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/agentes";
    }
}
