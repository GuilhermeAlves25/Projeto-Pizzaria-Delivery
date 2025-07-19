package br.com.pizzaria.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "clientes")
@PrimaryKeyJoinColumn(name = "id")
public class Cliente extends Usuario {

    private String telefone;

    @OneToMany(mappedBy = "cliente", fetch = FetchType.EAGER)
    private List<Pedido> pedidos;
}