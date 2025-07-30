package br.com.pizzaria.controller;

import br.com.pizzaria.model.Cliente;
import br.com.pizzaria.model.Endereco;
import br.com.pizzaria.security.CustomUserDetails;
import br.com.pizzaria.service.CarrinhoService;
import br.com.pizzaria.service.EnderecoService;
import br.com.pizzaria.service.PedidoService;
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

    private final EnderecoService enderecoService;
    private final CarrinhoService carrinhoService;
    private final PedidoService pedidoService;

    public EnderecoController(EnderecoService enderecoService, CarrinhoService carrinhoService, PedidoService pedidoService) {
        this.enderecoService = enderecoService;
        this.carrinhoService = carrinhoService;
        this.pedidoService = pedidoService;
    }

    @GetMapping("/novo")
    public String novoEnderecoForm(Model model) {
        model.addAttribute("endereco", new Endereco());
        return "fragments/endereco-form :: form-novo-endereco";
    }

    @PostMapping("/salvar")
    public String salvarNovoEndereco(@ModelAttribute Endereco endereco,
                                     @AuthenticationPrincipal CustomUserDetails userDetails,
                                     Model model) {


        enderecoService.salvarNovoEndereco(endereco, userDetails.getUsuario());


        Cliente cliente = (Cliente) userDetails.getUsuario();
        List<Endereco> enderecos = cliente.getEnderecos();

        if (!enderecos.contains(endereco)) {
            enderecos.add(endereco);
        }

        model.addAttribute("enderecos", enderecos);
        model.addAttribute("carrinho", carrinhoService);
        model.addAttribute("TAXA_ENTREGA", PedidoService.TAXA_ENTREGA);


        return "fragments/carrinho :: view-checkout";
    }
}