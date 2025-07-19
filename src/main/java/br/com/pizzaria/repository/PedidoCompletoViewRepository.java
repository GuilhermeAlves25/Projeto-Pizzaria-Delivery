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

    // Agora o nome do método corresponde à propriedade 'statusPedido' na entidade
    List<PedidoCompletoView> findByStatusPedidoOrderByDataHoraAsc(String status);

    // E este também corresponde às propriedades 'entregadorId' e 'statusPedido'
    List<PedidoCompletoView> findByEntregadorIdAndStatusPedido(Long entregadorId, String status);
    Page<PedidoCompletoView> findByDataHoraBetweenAndStatusPedido(LocalDateTime dataInicio, LocalDateTime dataFim, String status, Pageable pageable);
    //Page<PedidoCompletoView> findByDataHoraBetween(LocalDateTime dataInicio, LocalDateTime dataFim, Pageable pageable);
}