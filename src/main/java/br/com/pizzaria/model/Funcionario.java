package br.com.pizzaria.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "funcionarios")
@PrimaryKeyJoinColumn(name = "id")
public class Funcionario extends Usuario {

    @Enumerated(EnumType.STRING)
    private Cargo cargo;

    private BigDecimal salario;
    private LocalDate dataAdmissao;
    private LocalDate dataDemissao;
}