package br.com.pizzaria.controller;

import br.com.pizzaria.model.Funcionario;
import br.com.pizzaria.security.CustomUserDetails;
import br.com.pizzaria.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/entregador")
public class EntregadorController {

    @Autowired
    private PedidoService pedidoService;


    @GetMapping("/entregas")
    public String verEntregas(Model model, @AuthenticationPrincipal CustomUserDetails userDetails,
                              @RequestParam(name = "pageDisponiveis", defaultValue = "0") int pageDisponiveis,
                              @RequestParam(name = "pageMinhas", defaultValue = "0") int pageMinhas) {

        int pageSize = 4;


        model.addAttribute("pedidosParaEntrega",
                pedidoService.buscarPedidosProntosParaEntrega(PageRequest.of(pageDisponiveis, pageSize)));


        Funcionario entregadorLogado = (Funcionario) userDetails.getUsuario();
        model.addAttribute("minhasEntregas",
                pedidoService.buscarEntregasEmAndamento(entregadorLogado, PageRequest.of(pageMinhas, pageSize)));

        return "entregador/entregas";
    }



    
    @PostMapping("/iniciar-entrega")
    public String iniciarEntrega(@RequestParam("pedidoId") Long pedidoId,
                                 @AuthenticationPrincipal CustomUserDetails userDetails) {

        Funcionario entregador = (Funcionario) userDetails.getUsuario();
        pedidoService.iniciarEntrega(pedidoId, entregador);

        return "redirect:/entregador/entregas";
    }


    @PostMapping("/concluir-entrega")
    public String concluirEntrega(@RequestParam("pedidoId") Long pedidoId) {

        pedidoService.atualizarStatus(pedidoId, "ENTREGUE");
        return "redirect:/entregador/entregas";
    }
}