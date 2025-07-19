package br.com.pizzaria.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor // Construtor sem argumentos (exigido pelo JPA)
@AllArgsConstructor // Construtor com todos os argumentos (útil para testes e DTOs)
@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Lob
    private String descricao;

    private BigDecimal preco;
    private String tipo;
    private String tamanho;
    private String caminhoImagem;
}