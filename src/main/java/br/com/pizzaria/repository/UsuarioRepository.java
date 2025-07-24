package br.com.pizzaria.repository;

import br.com.pizzaria.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


    Optional<Usuario> findByEmail(String email);


    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.enderecos WHERE u.email = :email")
    Optional<Usuario> findByEmailWithEnderecos(@Param("email") String email);
}