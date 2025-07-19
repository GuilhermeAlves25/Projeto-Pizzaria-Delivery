package br.com.pizzaria.service;

import br.com.pizzaria.model.ItemPedido;
import br.com.pizzaria.model.Produto;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CarrinhoService implements Serializable {

    private static final long serialVersionUID = 1L;
    private final List<ItemPedido> itens = new ArrayList<>();

    private Optional<ItemPedido> findItemByProdutoId(Long produtoId) {
        return itens.stream()
                .filter(i -> Objects.equals(i.getProduto().getId(), produtoId))
                .findFirst();
    }

    public void adicionarItem(Produto produto) {
        ItemPedido item = findItemByProdutoId(produto.getId()).orElse(new ItemPedido());
        if (item.getProduto() == null) {
            item.setProduto(produto);
            item.setPrecoUnitario(produto.getPreco());
            item.setQuantidade(1);
            itens.add(item);
        } else {
            item.setQuantidade(item.getQuantidade() + 1);
        }
    }

    public void atualizarQuantidade(Long produtoId, int quantidade) {
        if (quantidade <= 0) {
            itens.removeIf(item -> Objects.equals(item.getProduto().getId(), produtoId));
        } else {
            findItemByProdutoId(produtoId).ifPresent(item -> item.setQuantidade(quantidade));
        }
    }

    public List<ItemPedido> getItens() { return itens; }

    public BigDecimal getValorTotal() {
        return itens.stream().map(ItemPedido::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void limpar() { itens.clear(); }
}