package br.com.pizzaria.repository;

import br.com.pizzaria.model.PedidoCompletoView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoCompletoViewRepository extends JpaRepository<PedidoCompletoView, Long> {


    List<PedidoCompletoView> findByStatusPedidoOrderByDataHoraAsc(String status);


    List<PedidoCompletoView> findByEntregadorIdAndStatusPedido(Long entregadorId, String status);
    Page<PedidoCompletoView> findByDataHoraBetweenAndStatusPedido(LocalDateTime dataInicio, LocalDateTime dataFim, String status, Pageable pageable);

}