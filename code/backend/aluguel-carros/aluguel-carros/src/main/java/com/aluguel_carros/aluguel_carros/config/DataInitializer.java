package com.aluguel_carros.aluguel_carros.config;

import com.aluguel_carros.aluguel_carros.model.Agente;
import com.aluguel_carros.aluguel_carros.model.Carro;
import com.aluguel_carros.aluguel_carros.model.Cliente;
import com.aluguel_carros.aluguel_carros.services.AgenteService;
import com.aluguel_carros.aluguel_carros.services.CarroService;
import com.aluguel_carros.aluguel_carros.services.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ClienteService clienteService;
    private final AgenteService agenteService;
    private final CarroService carroService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("Iniciando DataInitializer...");
        
        // Criar clientes de exemplo
        if (clienteService.listarTodos().isEmpty()) {
            log.info("Criando clientes de exemplo...");
            
            Cliente cliente1 = Cliente.builder()
                    .nome("Ana Silva")
                    .email("ana.silva@email.com")
                    .senha("123456") // Senha em texto plano - será criptografada pelo service
                    .telefone("(11) 99999-9999")
                    .endereco("Rua das Flores, 123 - São Paulo/SP")
                    .rg("12.345.678-9")
                    .cpf("123.456.789-00")
                    .profissao("Engenheira")
                    .rendimentosAuferidos(5000.00)
                    .build();

            Cliente cliente2 = Cliente.builder()
                    .nome("Carlos Santos")
                    .email("carlos.santos@email.com")
                    .senha("123456") // Senha em texto plano - será criptografada pelo service
                    .telefone("(11) 88888-8888")
                    .endereco("Av. Paulista, 456 - São Paulo/SP")
                    .rg("98.765.432-1")
                    .cpf("987.654.321-00")
                    .profissao("Advogado")
                    .rendimentosAuferidos(8000.00)
                    .build();

            clienteService.salvar(cliente1);
            clienteService.salvar(cliente2);
            log.info("Clientes criados com sucesso!");
        } else {
            log.info("Clientes já existem, pulando criação...");
        }

        // Criar agentes de exemplo
        if (agenteService.listarTodos().isEmpty()) {
            log.info("Criando agentes de exemplo...");
            
            Agente agente1 = Agente.builder()
                    .nome("João Silva")
                    .email("joao.silva@empresa.com")
                    .senha("123456") // Senha em texto plano - será criptografada pelo service
                    .codigoAgente("AGT001")
                    .departamento("Vendas")
                    .build();

            Agente agente2 = Agente.builder()
                    .nome("Maria Santos")
                    .email("maria.santos@empresa.com")
                    .senha("123456") // Senha em texto plano - será criptografada pelo service
                    .codigoAgente("AGT002")
                    .departamento("Atendimento")
                    .build();

            Agente agente3 = Agente.builder()
                    .nome("Pedro Costa")
                    .email("pedro.costa@empresa.com")
                    .senha("123456") // Senha em texto plano - será criptografada pelo service
                    .codigoAgente("AGT003")
                    .departamento("Gerência")
                    .build();

            agenteService.salvar(agente1);
            agenteService.salvar(agente2);
            agenteService.salvar(agente3);
            log.info("Agentes criados com sucesso!");
        } else {
            log.info("Agentes já existem, pulando criação...");
        }

        // Criar carros de exemplo
        if (carroService.listarTodos().isEmpty()) {
            log.info("Criando carros de exemplo...");
            
            Carro carro1 = Carro.builder()
                    .placa("ABC-1234")
                    .modelo("Corolla")
                    .marca("Toyota")
                    .cor("Prata")
                    .ano(2023)
                    .valorDiaria(new BigDecimal("150.00"))
                    .status(Carro.StatusCarro.DISPONIVEL)
                    .build();

            Carro carro2 = Carro.builder()
                    .placa("DEF-5678")
                    .modelo("Civic")
                    .marca("Honda")
                    .cor("Branco")
                    .ano(2022)
                    .valorDiaria(new BigDecimal("140.00"))
                    .status(Carro.StatusCarro.DISPONIVEL)
                    .build();

            Carro carro3 = Carro.builder()
                    .placa("GHI-9012")
                    .modelo("HB20")
                    .marca("Hyundai")
                    .cor("Preto")
                    .ano(2023)
                    .valorDiaria(new BigDecimal("120.00"))
                    .status(Carro.StatusCarro.DISPONIVEL)
                    .build();

            carroService.salvar(carro1);
            carroService.salvar(carro2);
            carroService.salvar(carro3);
            log.info("Carros criados com sucesso!");
        } else {
            log.info("Carros já existem, pulando criação...");
        }
        
        log.info("DataInitializer concluído!");
    }
}
