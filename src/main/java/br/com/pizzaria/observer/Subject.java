package br.com.pizzaria.observer;

public interface Subject {
    void registrarObservador(Observer observer);
    void removerObservador(Observer observer);
    void notificarObservadores();
}