package br.com.pizzaria.factory;

import br.com.pizzaria.model.Produto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProdutoFactory {


    public Produto criarProduto(String nome, String descricao, BigDecimal preco, String tipo, String tamanho, String caminhoImagem) {

        Produto novoProduto = new Produto();
        novoProduto.setNome(nome);
        novoProduto.setDescricao(descricao);
        novoProduto.setPreco(preco);


        if (tipo != null) {
            novoProduto.setTipo(tipo.toUpperCase());
        }
        if (tamanho != null) {
            novoProduto.setTamanho(tamanho.toUpperCase());
        }

        novoProduto.setCaminhoImagem(caminhoImagem);


        if ("BEBIDA".equals(novoProduto.getTipo())) {
            novoProduto.setTamanho("UNICO");
        }

        return novoProduto;
    }
}