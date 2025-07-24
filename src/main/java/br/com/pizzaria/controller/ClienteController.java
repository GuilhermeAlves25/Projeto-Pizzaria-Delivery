package br.com.pizzaria.controller;

import br.com.pizzaria.model.Cliente;
import br.com.pizzaria.model.Pedido;
import br.com.pizzaria.security.CustomUserDetails;
import br.com.pizzaria.service.PedidoService;
import br.com.pizzaria.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.print.Pageable;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/cardapio")
    public String verCardapio(Model model) {
        model.addAttribute("produtos", produtoService.listarTodosAtivos());
        return "cliente/cardapio";
    }


    @GetMapping("/meus-pedidos/recentes")
    public String getPedidosRecentes(Model model,
                                     @AuthenticationPrincipal CustomUserDetails userDetails,
                                     @RequestParam(defaultValue = "0") int page) {

        Page<Pedido> paginaDePedidos = pedidoService.buscarPedidosAtivosDoCliente(
                (Cliente) userDetails.getUsuario(),
                PageRequest.of(page, 3)
        );
        model.addAttribute("paginaDePedidos", paginaDePedidos);
        return "fragments/pedidos :: lista-pedidos";
    }


    @GetMapping("/meus-pedidos/{id}")
    public String getDetalhesPedido(@PathVariable("id") Long pedidoId, Model model) {
        model.addAttribute("pedido", pedidoService.buscarPorId(pedidoId));
        return "fragments/pedidos :: detalhes-pedido";
    }

    @GetMapping("/historico")
    public String verHistoricoDePedidos(Model model,
                                        @AuthenticationPrincipal CustomUserDetails userDetails,
                                        @RequestParam(name = "page", defaultValue = "0") int page) {


        Page<Pedido> paginaDePedidos = pedidoService.buscarHistoricoDoCliente(
                (Cliente) userDetails.getUsuario(),PageRequest.of(page, 5)
        );


        model.addAttribute("paginaDePedidos", paginaDePedidos);


        return "cliente/historico";
    }



}