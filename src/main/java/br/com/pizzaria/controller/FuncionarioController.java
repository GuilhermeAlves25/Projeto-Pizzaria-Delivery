package br.com.pizzaria.controller;

import br.com.pizzaria.model.Pedido;
import br.com.pizzaria.service.PedidoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Pageable;

import java.util.List;


@Controller
@RequestMapping("/funcionario")
public class FuncionarioController {


    private final PedidoService pedidoService;

    public FuncionarioController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/dashboard")
    public String verDashboard(Model model, @RequestParam(name = "page", defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, 6, Sort.by("dataHora").descending());
        Page<Pedido> paginaDePedidos = pedidoService.buscarTodosPedidosPaginado(pageable);
        model.addAttribute("paginaDePedidos", paginaDePedidos);
        return "funcionario/dashboard";
    }


    @PostMapping("/pedidos/preparar")
    public String prepararPedido(@RequestParam("pedidoId") Long pedidoId, RedirectAttributes redirectAttributes) {
        try {

            pedidoService.atualizarStatus(pedidoId, "PRONTO PARA ENTREGA");
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Status do pedido #" + pedidoId + " atualizado para PRONTO PARA ENTREGA.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao atualizar o status do pedido.");
        }
        return "redirect:/funcionario/dashboard";
    }


    @GetMapping("/historico")
    public String verHistorico(Model model,
                               @RequestParam(name = "page", defaultValue = "0") int page,
                               @RequestParam(name = "status", required = false) String status) { // Recebe o status

        Pageable pageable = PageRequest.of(page, 5, Sort.by("dataHora").descending());

        Page<Pedido> paginaDePedidos;

        // Se um status foi selecionado (e não é "TODOS"), filtra por ele.
        if (status != null && !status.isEmpty() && !status.equalsIgnoreCase("TODOS")) {
            List<String> statusList = List.of(status);
            paginaDePedidos = pedidoService.buscarPedidosPorStatus(statusList, pageable);
        } else {
            // Se nenhum status foi selecionado, busca todos os pedidos finalizados.
            paginaDePedidos = pedidoService.buscarHistoricoDeTodosPedidos(pageable);
        }

        model.addAttribute("paginaDePedidos", paginaDePedidos);
        model.addAttribute("statusFiltro", status); // Envia o status selecionado de volta para a tela

        return "funcionario/historico";
    }
}