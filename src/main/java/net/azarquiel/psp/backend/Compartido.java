package net.azarquiel.psp.backend;

public class Compartido {
    public String jugada;

    public Compartido() {
        jugada = "";
    }

    public synchronized void setJugada(String jugada) {
        this.jugada = jugada;
    }

    public synchronized String getJugada() {
        String result = this.jugada;
        this.jugada = "";
        return result;
    }
}
