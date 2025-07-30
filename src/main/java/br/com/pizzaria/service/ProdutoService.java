package br.com.pizzaria.service;

import br.com.pizzaria.model.Produto;
import br.com.pizzaria.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
public class ProdutoService {



    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public List<Produto> listarTodosAtivos() {
        return produtoRepository.findByAtivoTrue();
    }

    public List<Produto> listarTodosParaAdmin() {
        return produtoRepository.findAll();
    }


    public void alternarStatusProduto(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado!"));

        produto.setAtivo(!produto.isAtivo());
        produtoRepository.save(produto);
    }

    public Page<Produto> listarTodosParaAdminPaginado(Pageable pageable) {
        return produtoRepository.findAll(pageable);
    }
}