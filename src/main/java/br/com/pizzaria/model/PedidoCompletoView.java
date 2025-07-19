package br.com.pizzaria.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Immutable
@Table(name = "v_pedidos_completos")
@Getter
public class PedidoCompletoView {

    @Id
    @Column(name = "pedido_id")
    private Long pedidoId;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    @Column(name = "valor_total")
    private BigDecimal valorTotal;

    @Column(name = "status_pedido")
    private String statusPedido;

    @Column(name = "cliente_nome")
    private String clienteNome;

    @Column(name = "rua_entrega")
    private String ruaEntrega;

    @Column(name = "numero_entrega")
    private String numeroEntrega;

    @Column(name = "bairro_entrega")
    private String bairroEntrega;

    @Column(name = "entregador_id")
    private Long entregadorId;

    @Column(name = "entregador_nome")
    private String entregadorNome;

    @Column(name = "pagamento")
    private String pagamento;



}