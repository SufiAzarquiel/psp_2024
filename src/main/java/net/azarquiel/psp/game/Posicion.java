package net.azarquiel.psp.game;

public class Posicion {
    int fila;
    int columna;
    char letra;
    
    public synchronized void espera() {
        try {
            this.wait();
        }
        catch (InterruptedException ex) {}
    }
    
    public synchronized void despierto() {
        this.notify();
    }
    
    public synchronized void cargaPosicion(final int pfila, final int pcolumna) {
        this.fila = pfila;
        this.columna = pcolumna;
    }
    
    public synchronized void cargaLetra(final char pletra) {
        this.letra = pletra;
    }
    
    public synchronized int fila() {
        return this.fila;
    }
    
    public synchronized int columna() {
        return this.columna;
    }
    
    public synchronized char letra() {
        return this.letra;
    }
    
    public synchronized char otraletra() {
        return (this.letra == 'X') ? 'O' : 'X';
    }
}
