package br.com.pizzaria.repository;

import br.com.pizzaria.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    // Métodos customizados para produtos viriam aqui.
    // Ex: List<Produto> findByTipo(String tipo);
}