package br.com.pizzaria.model;

import br.com.pizzaria.observer.Observer;
import br.com.pizzaria.observer.Subject;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "pedidos")
public class Pedido implements Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_endereco_entrega", nullable = false)
    private Endereco enderecoDeEntrega;

    @ManyToOne
    @JoinColumn(name = "id_funcionario_entregador")
    private Funcionario entregador;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Column(nullable = false)
    private BigDecimal valorTotal;

    @Column(nullable = false)
    private String statusPedido;

    // NOVOS CAMPOS DO BANCO
    @Column(nullable = false)
    private String pagamento;

    @Column(nullable = false)
    private String tipoEntrega;


    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ItemPedido> itens;


    @Transient
    private final List<Observer> observadores = new ArrayList<>();


    public void setStatusPedido(String novoStatus) {
        this.statusPedido = novoStatus;
        System.out.println("Status do pedido #" + this.id + " alterado para: " + novoStatus);
        notificarObservadores(); // Dispara a notificação
    }

    @Override
    public void registrarObservador(Observer observer) {
        this.observadores.add(observer);
    }

    @Override
    public void removerObservador(Observer observer) {
        this.observadores.remove(observer);
    }

    @Override
    public void notificarObservadores() {
        for (Observer obs : this.observadores) {
            obs.atualizar(this);
        }
    }
}