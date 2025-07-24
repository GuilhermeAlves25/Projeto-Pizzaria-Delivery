package br.com.pizzaria.service;

import br.com.pizzaria.model.*;
import br.com.pizzaria.observer.SseNotificationObserver;
import br.com.pizzaria.repository.PedidoCompletoViewRepository;
import br.com.pizzaria.repository.PedidoRepository;
import br.com.pizzaria.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private PedidoCompletoViewRepository pedidoCompletoViewRepository;

    public static final BigDecimal TAXA_ENTREGA = new BigDecimal("5.00");

    @Transactional

    public Pedido criarPedido(CarrinhoService carrinhoService, Long idEndereco, String formaPagamento, CustomUserDetails userDetails) {
        Pedido novoPedido = new Pedido();


        if (userDetails == null || !(userDetails.getUsuario() instanceof Cliente)) {
            throw new IllegalStateException("Usuário logado não é um cliente válido.");
        }
        Cliente cliente = (Cliente) userDetails.getUsuario();


        Endereco endereco = cliente.getEnderecos().stream()
                .filter(e -> e.getId().equals(idEndereco))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado para o cliente."));


        novoPedido.setCliente(cliente);
        novoPedido.setEnderecoDeEntrega(endereco);
        novoPedido.setDataHora(LocalDateTime.now());
        novoPedido.setStatusPedido("RECEBIDO");
        novoPedido.setPagamento(formaPagamento);
        novoPedido.setTipoEntrega("DELIVERY");

        BigDecimal valorTotal = carrinhoService.getValorTotal().add(TAXA_ENTREGA);
        novoPedido.setValorTotal(valorTotal);

        List<ItemPedido> itensDoPedido = new ArrayList<>();
        carrinhoService.getItens().forEach(itemCarrinho -> {
            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setPedido(novoPedido);
            itemPedido.setProduto(itemCarrinho.getProduto());
            itemPedido.setQuantidade(itemCarrinho.getQuantidade());
            itemPedido.setPrecoUnitario(itemCarrinho.getPrecoUnitario());
            itensDoPedido.add(itemPedido);
        });
        novoPedido.setItens(itensDoPedido);


        pedidoRepository.save(novoPedido);


        cliente.getPedidos().add(novoPedido);


        carrinhoService.limpar();

        return novoPedido;
    }

  //  public List<Pedido> buscarPedidosRecebidos() {
       // return pedidoRepository.findAllByStatusPedidoOrderByDataHoraAsc("RECEBIDO");
    //}

    public Page<Pedido> buscarTodosPedidosPaginado(Pageable pageable) {

        return pedidoRepository.findAll(pageable);
    }

    public Page<Pedido> buscarHistoricoDeTodosPedidos(Pageable pageable) {

        List<String> statusHistorico = List.of("ENTREGUE", "CANCELADO","RECEBIDO");

        return pedidoRepository.findAllByStatusPedidoIn(statusHistorico, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Pedido> buscarPedidosAtivosDoCliente(Cliente cliente, Pageable pageable) {

        List<String> statusAtivos = List.of("RECEBIDO", "EM PREPARO", "PRONTO PARA ENTREGA","SAIU PARA ENTREGA");


        return pedidoRepository.findByClienteIdAndStatusPedidoInOrderByDataHoraDesc(cliente.getId(), statusAtivos, pageable);
    }

    public Pedido buscarPorId(Long pedidoId) {
        return pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }



    @Transactional
    public void cancelarPedido(Long pedidoId, Cliente cliente) {

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado!"));

        if (!pedido.getCliente().getId().equals(cliente.getId())) {
            throw new SecurityException("Acesso negado. Você não pode cancelar este pedido.");
        }


        if ("RECEBIDO".equals(pedido.getStatusPedido())) {

            pedidoRepository.cancelarPedidoProcedure(pedidoId);
        } else {

            throw new IllegalStateException("Não é possível cancelar o pedido, pois ele já está em preparação.");
        }
    }

    public Page<Pedido> buscarHistoricoDoCliente(Cliente cliente, Pageable pageable) {
        // Define a lista de status que consideramos como histórico
        List<String> statusFinalizados = List.of("ENTREGUE", "CANCELADO");
        // Chama o método do repositório para buscar os pedidos
        return pedidoRepository.findByClienteIdAndStatusPedidoInOrderByDataHoraDesc(cliente.getId(), statusFinalizados, pageable);
    }

    public Page<Pedido> buscarHistoricoCompletoDoCliente(Cliente cliente, Pageable pageable) {
        return pedidoRepository.findByClienteIdOrderByDataHoraDesc(cliente.getId(), pageable);
    }

    @Transactional
    public void atualizarStatus(Long pedidoId, String novoStatus) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado!"));


        pedido.registrarObservador(new SseNotificationObserver());


        pedido.setStatusPedido(novoStatus);

        pedidoRepository.save(pedido);
    }

    //public List<Pedido> buscarPedidosProntosParaEntrega() {

        //return pedidoRepository.findByStatusPedidoComDetalhesCompletos("PRONTO PARA ENTREGA");
    //}
        public Page<Pedido> buscarPedidosProntosParaEntrega(Pageable pageable) {
            return pedidoRepository.findByStatusPedidoComDetalhesCompletos("PRONTO PARA ENTREGA", pageable);
        }



    @Transactional
    public void iniciarEntrega(Long pedidoId, Funcionario entregador) {

        if (entregador.getCargo() != Cargo.ENTREGADOR) {
            throw new SecurityException("Apenas entregadores podem iniciar uma entrega.");
        }

        pedidoRepository.atribuirEntregadorProcedure(pedidoId, entregador.getId());

        Pedido pedidoAtualizado = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado após atualização."));

        new SseNotificationObserver().atualizar(pedidoAtualizado);


    }

    public Page<PedidoCompletoView> buscarPedidosPorPeriodoView(LocalDateTime dataInicio, LocalDateTime dataFim, Pageable pageable) {

        return pedidoCompletoViewRepository.findByDataHoraBetweenAndStatusPedido(dataInicio, dataFim, "ENTREGUE", pageable);
    }

    public BigDecimal calcularFaturamentoPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {

        BigDecimal total = pedidoRepository.sumValorTotalByStatusAndDataHoraBetween("ENTREGUE", dataInicio, dataFim);
        return total != null ? total : BigDecimal.ZERO;
    }



    public Page<Pedido> buscarEntregasEmAndamento(Funcionario entregador, Pageable pageable) {
        return pedidoRepository.findByEntregadorIdAndStatusPedido(entregador.getId(), "SAIU PARA ENTREGA", pageable);
    }
}