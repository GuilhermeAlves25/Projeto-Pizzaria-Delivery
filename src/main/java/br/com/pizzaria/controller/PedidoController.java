package br.com.pizzaria.controller;

import br.com.pizzaria.model.Cliente;
import br.com.pizzaria.model.Endereco;
import br.com.pizzaria.model.Pedido;
import br.com.pizzaria.security.CustomUserDetails;
import br.com.pizzaria.service.CarrinhoService;
import br.com.pizzaria.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private CarrinhoService carrinhoService;

    @GetMapping("/checkout")
    public String checkoutView(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) principal;
            Cliente cliente = (Cliente) userDetails.getUsuario();
            List<Endereco> enderecos = cliente.getEnderecos();
            model.addAttribute("enderecos", enderecos);
        } else {
            return "redirect:/login";
        }

        model.addAttribute("carrinho", carrinhoService);
        model.addAttribute("TAXA_ENTREGA", PedidoService.TAXA_ENTREGA);

        return "fragments/carrinho :: view-checkout";
    }

    @PostMapping("/finalizar")
    // AQUI ESTÁ A CORREÇÃO: Adicionamos o @AuthenticationPrincipal para que o Spring injete o usuário.
    public String finalizarPedido(@RequestParam("idEndereco") Long idEndereco,
                                  @RequestParam("formaPagamento") String formaPagamento,
                                  @AuthenticationPrincipal CustomUserDetails userDetails,
                                  RedirectAttributes redirectAttributes) {

        if (carrinhoService.getItens().isEmpty()) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Seu carrinho está vazio!");
            return "redirect:/cliente/cardapio";
        }
        try {
            // Agora a variável 'userDetails' existe e pode ser passada para o serviço.
            pedidoService.criarPedido(carrinhoService, idEndereco, formaPagamento, userDetails);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Pedido recebido com sucesso!");
            return "redirect:/cliente/cardapio";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao finalizar o pedido: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/cliente/cardapio";
        }
    }

    @PostMapping("/cancelar")
    public String cancelarPedido(@RequestParam("pedidoId") Long pedidoId,
                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                 RedirectAttributes redirectAttributes) { // Adicionamos RedirectAttributes

        Cliente cliente = (Cliente) userDetails.getUsuario();
        try {
            pedidoService.cancelarPedido(pedidoId, cliente);
            // Se tudo der certo, mostra uma mensagem de sucesso
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Pedido #" + pedidoId + " cancelado com sucesso!");
        } catch (Exception e) {
            // Se o serviço lançar uma exceção, captura a mensagem e mostra como erro
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
        }

        // Redireciona de volta para a página de onde o usuário veio (ou para a home/cardápio)
        return "redirect:/cliente/cardapio";
    }

    @GetMapping("/test/update/{pedidoId}/{status}")
    @ResponseBody // Retorna apenas texto, não uma página HTML
    public String testUpdateStatus(@PathVariable Long pedidoId, @PathVariable String status) {

        // Esta chamada irá acionar toda a lógica do padrão Observer
        pedidoService.atualizarStatus(pedidoId, status);

        return "Evento de atualização para o pedido #" + pedidoId + " com o status '" + status + "' foi disparado!";
    }

    @GetMapping("/fragments/pedidos")
    public String getPedidosRecentes(Model model,
                                     @AuthenticationPrincipal CustomUserDetails userDetails,
                                     @RequestParam(defaultValue = "0") int page) {


        Page<Pedido> paginaDePedidos = pedidoService.buscarPedidosAtivosDoCliente(
                (Cliente) userDetails.getUsuario(),
                PageRequest.of(page, 3) // 3 pedidos por página
        );
        model.addAttribute("paginaDePedidos", paginaDePedidos);
        return "fragments/pedidos :: lista-pedidos";
    }

}