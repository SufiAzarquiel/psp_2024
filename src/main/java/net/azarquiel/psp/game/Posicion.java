package net.azarquiel.psp.game; // Define el paquete

public class Posicion { // Define la clase Posicion
    int fila; // Variable para almacenar la fila
    int columna; // Variable para almacenar la columna
    char letra; // Variable para almacenar la letra (X o O)
    private String nombreJugador; // Variable para almacenar el nombre del jugador
    private String nombreOponente; // Variable para almacenar el nombre del oponente

    // Método sincronizado para hacer que el hilo espere
    public synchronized void espera() {
        try {
            this.wait(120*1000); // El hilo espera hasta que es notificado por otro hilo o que pasen 2 minutos
        } catch (InterruptedException ex) {
            // Captura y manejo de la excepción de interrupción del hilo
        }
    }

    // Método sincronizado para notificar a un hilo en espera
    public synchronized void despierto() {
        this.notify(); // Notifica a un hilo que está esperando
    }

    // Método sincronizado para establecer la posición
    public synchronized void cargaPosicion(final int pfila, final int pcolumna) {
        this.fila = pfila; // Establece la fila
        this.columna = pcolumna; // Establece la columna
    }

    // Método sincronizado para establecer la letra
    public synchronized void cargaLetra(final char pletra) {
        this.letra = pletra; // Establece la letra (X o O)
    }

    // Método sincronizado para obtener la fila
    public synchronized int fila() {
        return this.fila; // Retorna la fila
    }

    // Método sincronizado para obtener la columna
    public synchronized int columna() {
        return this.columna; // Retorna la columna
    }

    // Método sincronizado para obtener la letra
    public synchronized char letra() {
        return this.letra; // Retorna la letra (X o O)
    }

    // Método sincronizado para obtener la otra letra (X si la letra actual es O, y viceversa)
    public synchronized char otraletra() {
        return (this.letra == 'X') ? 'O' : 'X'; // Retorna la otra letra
    }

    // Método sincronizado para obtener el nombre del jugador
    public synchronized String nombreJugador() {
        return this.nombreJugador;
    }

    // Método sincronizado para establecer el nombre del jugador
    public synchronized void cargaNombreJugador(final String pnombreJugador) {
        this.nombreJugador = pnombreJugador;
    }

    // Método sincronizado para obtener el nombre del oponente
    public synchronized String nombreOponente() {
        return this.nombreOponente;
    }

    // Método sincronizado para establecer el nombre del oponente
    public synchronized void cargaNombreOponente(final String pnombreOponente) {
        this.nombreOponente = pnombreOponente;
    }
}
