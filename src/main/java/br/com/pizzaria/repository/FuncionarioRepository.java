package br.com.pizzaria.repository;

import br.com.pizzaria.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    // Métodos customizados para funcionários viriam aqui.
    // Ex: List<Funcionario> findByCargo(Cargo cargo);
}