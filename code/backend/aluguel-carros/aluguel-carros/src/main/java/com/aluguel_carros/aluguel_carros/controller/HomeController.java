package com.aluguel_carros.aluguel_carros.controller;

import com.aluguel_carros.aluguel_carros.model.UsuarioAuth;
import com.aluguel_carros.aluguel_carros.services.AluguelService;
import com.aluguel_carros.aluguel_carros.services.CarroService;
import com.aluguel_carros.aluguel_carros.services.ClienteService;
import com.aluguel_carros.aluguel_carros.services.UsuarioAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    
    private final ClienteService clienteService;
    private final CarroService carroService;
    private final AluguelService aluguelService;
    private final UsuarioAuthService usuarioAuthService;
    
    @GetMapping("/")
    public String home(Model model) {
        // Estatísticas para o dashboard
        long totalClientes = clienteService.listarTodos().size();
        long totalCarros = carroService.listarTodos().size();
        long carrosDisponiveis = carroService.buscarDisponiveis().size();
        long totalAlugueis = aluguelService.listarTodos().size();
        long alugueisAtivos = aluguelService.buscarPorStatus(com.aluguel_carros.aluguel_carros.model.Aluguel.StatusAluguel.ATIVO).size();
        
        model.addAttribute("totalClientes", totalClientes);
        model.addAttribute("totalCarros", totalCarros);
        model.addAttribute("carrosDisponiveis", carrosDisponiveis);
        model.addAttribute("totalAlugueis", totalAlugueis);
        model.addAttribute("alugueisAtivos", alugueisAtivos);
        
        // Informações do usuário logado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            String email = auth.getName();
            UsuarioAuth usuario = usuarioAuthService.buscarPorEmail(email).orElse(null);
            model.addAttribute("usuarioLogado", usuario);
        }
        
        return "index";
    }
}
