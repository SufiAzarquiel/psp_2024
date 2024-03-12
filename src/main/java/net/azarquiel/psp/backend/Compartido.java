package net.azarquiel.psp.backend; // Define el paquete

public class Compartido { // Define la clase Compartido
    private int jugadores; // Variable para almacenar el número de jugadores

    // Constructor de la clase Compartido
    public Compartido() {
        jugadores = 0; // Inicializa el número de jugadores como 0
    }

    // Método sincronizado para incrementar el número de jugadores
    public synchronized void incrementa() {
        jugadores++; // Incrementa el número de jugadores de forma segura para hilos concurrentes
    }

    // Método sincronizado para decrementar el número de jugadores
    public synchronized void decrementa() {
        jugadores--; // Decrementa el número de jugadores de forma segura para hilos concurrentes
    }

    // Método sincronizado para obtener el número de jugadores
    public synchronized int jugadores() {
        return jugadores; // Retorna el número de jugadores de forma segura para hilos concurrentes
    }
}
