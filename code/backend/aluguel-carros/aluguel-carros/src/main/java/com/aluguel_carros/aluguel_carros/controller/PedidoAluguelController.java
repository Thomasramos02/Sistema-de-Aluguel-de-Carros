package com.aluguel_carros.aluguel_carros.controller;

import com.aluguel_carros.aluguel_carros.model.Carro;
import com.aluguel_carros.aluguel_carros.model.Cliente;
import com.aluguel_carros.aluguel_carros.model.PedidoAluguel;
import com.aluguel_carros.aluguel_carros.services.CarroService;
import com.aluguel_carros.aluguel_carros.services.ClienteService;
import com.aluguel_carros.aluguel_carros.services.PedidoAluguelService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoAluguelController {
    
    private final PedidoAluguelService pedidoAluguelService;
    private final ClienteService clienteService;
    private final CarroService carroService;
    
    @GetMapping("/novo")
    public String novoPedido(Model model) {
        // Obter cliente logado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Cliente cliente = clienteService.buscarPorEmail(email)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado. Faça login primeiro."));
        
        List<Carro> carrosDisponiveis = carroService.buscarDisponiveis();
        model.addAttribute("carrosDisponiveis", carrosDisponiveis);
        model.addAttribute("pedido", new PedidoAluguel());
        model.addAttribute("cliente", cliente);
        return "pedidos/form";
    }
    
    @PostMapping("/novo")
    public String criarPedido(@RequestParam Long carroId,
                             @RequestParam String dataInicio,
                             @RequestParam String dataFim,
                             @RequestParam(required = false) String observacoesCliente,
                             RedirectAttributes redirectAttributes) {
        try {
            // Obter cliente logado automaticamente
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            Cliente cliente = clienteService.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado. Faça login primeiro."));
            
            // Converter strings para LocalDateTime
            LocalDateTime inicio = LocalDateTime.parse(dataInicio);
            LocalDateTime fim = LocalDateTime.parse(dataFim);
            
            // Cliente faz pedido para si próprio
            PedidoAluguel pedido = pedidoAluguelService.solicitarAluguel(
                cliente.getId(), carroId, inicio, fim, observacoesCliente);
            
            redirectAttributes.addFlashAttribute("success", "Pedido de aluguel enviado com sucesso! Aguarde a análise do agente.");
            return "redirect:/pedidos/meus-pedidos";
            
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/pedidos/novo";
        }
    }
    
    @GetMapping("/meus-pedidos")
    public String meusPedidos(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Cliente cliente = clienteService.buscarPorEmail(email)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        List<PedidoAluguel> pedidos = pedidoAluguelService.buscarPorCliente(cliente.getId());
        model.addAttribute("pedidos", pedidos);
        model.addAttribute("cliente", cliente);
        return "pedidos/meus-pedidos";
    }
    
    @GetMapping("/{id}")
    public String visualizarPedido(@PathVariable Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Cliente cliente = clienteService.buscarPorEmail(email)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        PedidoAluguel pedido = pedidoAluguelService.buscarPorIdECliente(id, cliente.getId())
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        
        model.addAttribute("pedido", pedido);
        return "pedidos/detalhes";
    }
}
