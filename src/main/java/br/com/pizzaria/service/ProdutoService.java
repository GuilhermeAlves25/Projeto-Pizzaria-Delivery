package br.com.pizzaria.service;

import br.com.pizzaria.model.Produto;
import br.com.pizzaria.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {


    @Autowired
    private ProdutoRepository produtoRepository;


    public List<Produto> listarTodosAtivos() {
        return produtoRepository.findByAtivoTrue();
    }

    public List<Produto> listarTodosParaAdmin() {
        return produtoRepository.findAll();
    }

    @Transactional
    public void alternarStatusProduto(Long id) {
        // 1. Busca o produto no banco
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado!"));

        // 2. Inverte o valor booleano atual (se for true, vira false; se for false, vira true)
        produto.setAtivo(!produto.isAtivo());

        // 3. Salva o produto com o novo status
        produtoRepository.save(produto);
    }
}