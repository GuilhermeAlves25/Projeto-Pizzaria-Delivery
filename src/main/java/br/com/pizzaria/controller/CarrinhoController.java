package br.com.pizzaria.controller;

import br.com.pizzaria.model.Produto;
import br.com.pizzaria.service.CarrinhoService;
import br.com.pizzaria.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/carrinho")
public class CarrinhoController {

    @Autowired
    private CarrinhoService carrinhoService;

    @Autowired
    private ProdutoService produtoService;

    private String retornarViewDoCarrinho(Model model) {
        model.addAttribute("carrinho", carrinhoService);
        return "fragments/carrinho :: view-carrinho";
    }

    @GetMapping("/ver")
    public String verCarrinho(Model model) {
        return retornarViewDoCarrinho(model);
    }

    @PostMapping("/adicionar/{produtoId}")
    public String adicionarAoCarrinho(@PathVariable("produtoId") Long produtoId, Model model) {
        Produto produto = produtoService.listarTodos().stream()
                .filter(p -> p.getId().equals(produtoId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Produto não encontrado com ID: " + produtoId));
        carrinhoService.adicionarItem(produto);
        return retornarViewDoCarrinho(model);
    }

    @PostMapping("/atualizar")
    public String atualizarQuantidade(@RequestParam("produtoId") Long produtoId,
                                      @RequestParam("quantidade") int quantidade, Model model) {
        carrinhoService.atualizarQuantidade(produtoId, quantidade);
        return retornarViewDoCarrinho(model);
    }
}