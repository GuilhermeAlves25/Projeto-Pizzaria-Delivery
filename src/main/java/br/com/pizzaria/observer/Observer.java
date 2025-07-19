package br.com.pizzaria.observer;

import br.com.pizzaria.model.Pedido;

// A interface do observador agora passa o Pedido para dar mais contexto
public interface Observer {
    void atualizar(Pedido pedido);
}