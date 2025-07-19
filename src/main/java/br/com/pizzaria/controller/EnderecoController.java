package br.com.pizzaria.controller;

import br.com.pizzaria.model.Cliente;
import br.com.pizzaria.model.Endereco;
import br.com.pizzaria.security.CustomUserDetails;
import br.com.pizzaria.service.CarrinhoService;
import br.com.pizzaria.service.EnderecoService;
import br.com.pizzaria.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/endereco")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    // Precisamos injetar estes serviços para poder montar a tela de checkout
    @Autowired
    private CarrinhoService carrinhoService;

    @Autowired
    private PedidoService pedidoService;


    @GetMapping("/novo")
    public String novoEnderecoForm(Model model) {
        model.addAttribute("endereco", new Endereco());
        return "fragments/endereco-form :: form-novo-endereco";
    }

    @PostMapping("/salvar")
    public String salvarNovoEndereco(@ModelAttribute Endereco endereco,
                                     @AuthenticationPrincipal CustomUserDetails userDetails,
                                     Model model) {

        // 1. Salva o novo endereço no banco de dados
        enderecoService.salvarNovoEndereco(endereco, userDetails.getUsuario());

        // 2. Prepara o modelo para renderizar novamente a tela de checkout ATUALIZADA
        Cliente cliente = (Cliente) userDetails.getUsuario();
        List<Endereco> enderecos = cliente.getEnderecos();
        // A linha abaixo garante que a lista de endereços no objeto de sessão seja atualizada
        if (!enderecos.contains(endereco)) {
            enderecos.add(endereco);
        }

        model.addAttribute("enderecos", enderecos);
        model.addAttribute("carrinho", carrinhoService);
        model.addAttribute("TAXA_ENTREGA", PedidoService.TAXA_ENTREGA);

        // 3. Retorna o fragmento HTML da tela de checkout para o JavaScript
        return "fragments/carrinho :: view-checkout";
    }
}