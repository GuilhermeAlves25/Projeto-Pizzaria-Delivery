package br.com.pizzaria.observer;

import br.com.pizzaria.controller.NotificationController;
import br.com.pizzaria.model.Pedido;

public class StatusUpdateObserver implements Observer {

    private final String novoStatus;


    public StatusUpdateObserver(String novoStatus) {
        this.novoStatus = novoStatus;
    }

    @Override
    public void atualizar(Pedido pedido) {
        String emailCliente = pedido.getCliente().getEmail();
        String mensagem = "Seu pedido #" + pedido.getId() + " foi atualizado para: " + this.novoStatus;

        NotificationController.sendEventToUser(emailCliente, mensagem);
    }
}