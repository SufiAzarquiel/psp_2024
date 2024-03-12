package net.azarquiel.psp.backend;

public class Compartido {
    private int jugadores;
    public Compartido() {
        jugadores = 0;
    }

    public synchronized void incrementa() {
        jugadores++;
    }

    public synchronized void decrementa() {
        jugadores--;
    }

    public synchronized int jugadores() {
        return jugadores;
    }
}
