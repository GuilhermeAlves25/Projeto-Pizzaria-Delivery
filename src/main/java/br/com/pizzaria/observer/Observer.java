package br.com.pizzaria.observer;

import br.com.pizzaria.model.Pedido;


public interface Observer {
    void atualizar(Pedido pedido);
}