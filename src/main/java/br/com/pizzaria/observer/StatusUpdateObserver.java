package br.com.pizzaria.observer;

import br.com.pizzaria.controller.NotificationController;
import br.com.pizzaria.model.Pedido;

public class StatusUpdateObserver implements Observer {

    private final String novoStatus;

    // O construtor agora recebe o novo status
    public StatusUpdateObserver(String novoStatus) {
        this.novoStatus = novoStatus;
    }

    @Override
    public void atualizar(Pedido pedido) {
        String emailCliente = pedido.getCliente().getEmail();
        // Usa o novoStatus que foi passado no construtor
        String mensagem = "Seu pedido #" + pedido.getId() + " foi atualizado para: " + this.novoStatus;

        NotificationController.sendEventToUser(emailCliente, mensagem);
    }
}