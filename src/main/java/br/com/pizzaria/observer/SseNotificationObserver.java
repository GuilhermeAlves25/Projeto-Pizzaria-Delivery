package br.com.pizzaria.observer;

import br.com.pizzaria.controller.NotificationController;
import br.com.pizzaria.model.Pedido;

public class SseNotificationObserver implements Observer {

    @Override
    public void atualizar(Pedido pedido) {


        String emailCliente = pedido.getCliente().getEmail();
        String status = pedido.getStatusPedido();
        String mensagem = "Pedido #" + pedido.getId() + " mudou o status para: " + status;


        NotificationController.sendEventToUser(emailCliente, mensagem);
    }
}