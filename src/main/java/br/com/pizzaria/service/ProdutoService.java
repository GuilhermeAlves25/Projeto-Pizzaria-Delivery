package br.com.pizzaria.service;

import br.com.pizzaria.model.Produto;
import br.com.pizzaria.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {


    @Autowired
    private ProdutoRepository produtoRepository;


    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }
}