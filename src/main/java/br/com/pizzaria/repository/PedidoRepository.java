package br.com.pizzaria.repository;

import br.com.pizzaria.model.Pedido;
// AQUI ESTÃO AS IMPORTAÇÕES CORRETAS
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {


    List<Pedido> findByClienteIdOrderByDataHoraDesc(Long clienteId);

    Page<Pedido> findAllByStatusPedidoIn(List<String> statuses, Pageable pageable);

    Page<Pedido> findByClienteIdAndStatusPedidoInOrderByDataHoraDesc(Long clienteId, List<String> statuses, Pageable pageable);
    Page<Pedido> findByClienteIdOrderByDataHoraDesc(Long clienteId, Pageable pageable);
    List<Pedido> findByStatusPedidoOrderByDataHoraAsc(String status);

    Page<Pedido> findByEntregadorIdAndStatusPedido(Long entregadorId, String status, Pageable pageable);

    Page<Pedido> findByStatusPedidoAndDataHoraBetween(String status, LocalDateTime dataInicio, LocalDateTime dataFim, Pageable pageable);


    List<Pedido> findAllByStatusPedidoOrderByDataHoraAsc(String status);

    @Query("SELECT p FROM Pedido p " +
            "JOIN FETCH p.cliente c " +
            "JOIN FETCH p.enderecoDeEntrega e " +
            "JOIN FETCH p.itens i " +
            "JOIN FETCH i.produto " +
            "WHERE p.statusPedido = :status " +
            "ORDER BY p.dataHora ASC")
    Page<Pedido> findByStatusPedidoComDetalhesCompletos(@Param("status") String status,Pageable pageable);

    @Procedure("sp_cancelar_pedido")
    void cancelarPedidoProcedure(@Param("p_id_pedido") Long pedidoId);

    @Procedure("sp_atribuir_entregador")
    void atribuirEntregadorProcedure(@Param("p_id_pedido") Long pedidoId,
                                     @Param("p_id_entregador") Long entregadorId);


    @Query("SELECT SUM(p.valorTotal) FROM Pedido p WHERE p.statusPedido = :status AND p.dataHora BETWEEN :dataInicio AND :dataFim")
    BigDecimal sumValorTotalByStatusAndDataHoraBetween(@Param("status") String status,
                                                       @Param("dataInicio") LocalDateTime dataInicio,
                                                       @Param("dataFim") LocalDateTime dataFim);

    //@Query("SELECT SUM(p.valorTotal) FROM Pedido p WHERE p.dataHora BETWEEN :dataInicio AND :dataFim")
    //BigDecimal sumValorTotalByDataHoraBetween(@Param("dataInicio") LocalDateTime dataInicio, @Param("dataFim") LocalDateTime dataFim);
}