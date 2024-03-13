package net.azarquiel.psp.game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Logica extends Thread {
    int jugador; // Número de jugador
    Socket socketJugador; // Socket para la conexión del jugador
    DataOutputStream salida; // Flujo de salida para enviar datos al otro jugador
    DataInputStream entrada; // Flujo de entrada para recibir datos del otro jugador
    Tablero tablero; // Instancia del tablero de juego
    Posicion compartidoPos; // Instancia compartida para determinar la posición actual en el tablero
    String nombreJugador; // Nombre del jugador

    // Constructor
    public Logica(final Tablero pt, final Posicion pp) {
        this.socketJugador = null;
        this.salida = null;
        this.entrada = null;
        this.tablero = pt;
        this.compartidoPos = pp;
    }

    // Método para enviar el nombre del jugador al otro jugador
    public void enviarNombre(String nombre) {
        try {
            this.salida.writeUTF(nombre);
        } catch (IOException ex) {
            System.out.println("Error al enviar el nombre del jugador");
        }
    }

    // Método para recibir el nombre del otro jugador
    public String recibirNombre() {
        try {
            return this.entrada.readUTF();
        } catch (IOException ex) {
            System.out.println("Error al recibir el nombre del jugador");
            return null;
        }
    }

    // Método para conectar con el servidor
    public void Conecto() {
        String host = "nube1.sufiazarquiel.com";
        int puerto = 3030;
        boolean termino = false;
        while (!termino) {
            termino = true;
            try {
                this.socketJugador = new Socket(host, puerto);
            } catch (IOException e) {
                termino = false;
                System.out.printf("Error al conectar con el servidor por el puerto: %d.\nHost: %s\n", puerto, host);
            }
        }
        try {
            this.salida = new DataOutputStream(this.socketJugador.getOutputStream());
            this.entrada = new DataInputStream(this.socketJugador.getInputStream());

        } catch (IOException ex) {
            System.out.println("Error al crear los flujos de entrada y salida");
        }
    }

    // Método para obtener el turno del jugador
    public int turno() {
        int turno = -1;
        try {
            turno = this.entrada.readInt();
        } catch (IOException ex) {
            System.out.println("Error al leer turno");
        }
        return turno;
    }

    // Método para iniciar el hilo de la lógica del juego
    public void inicio(final int pj) {
        this.jugador = pj;
        this.start();
    }

    // Método que define la lógica del juego
    @Override
    public void run() {
        boolean esMiTurno = this.jugador == 1;
        int fila = 0;
        int columna = 0;
        while (this.tablero.hueco() && !this.tablero.enraya()) {
            if (esMiTurno) {
                this.tablero.Activo();
                this.compartidoPos.espera(); // Esperar a que el jugador actual realice su movimiento
                try {
                    this.salida.writeInt(this.compartidoPos.fila());
                    this.salida.writeInt(this.compartidoPos.columna());
                } catch (IOException ex) {
                    System.out.println("Error al comunicar nueva posicion al tablero");
                }
            } else {
                this.tablero.Desactivo();
                try {
                    fila = this.entrada.readInt();
                    columna = this.entrada.readInt();
                    this.tablero.Poner(fila, columna, this.compartidoPos.otraletra());
                } catch (IOException ex2) {
                    System.out.println("Error al leer nueva posicion del tablero");
                }
            }
            esMiTurno = !esMiTurno; // Cambiar al turno del otro jugador
            try {
                Thread.sleep(500L); // Esperar un tiempo antes de verificar el estado del juego nuevamente
            } catch (InterruptedException ex3) {
                System.out.println("Hilo de logica interrumpido");
            }
        }
        if (!this.tablero.hueco()) {
            this.tablero.tablas(); // Si no hay más espacios disponibles en el tablero, se declara un empate
        }

        if (this.tablero.enraya()) {
            this.tablero.gano(); // Si alguien ha ganado, mostrar el mensaje de victoria
        }
        try {
            Thread.sleep(2000L); // Esperar antes de cerrar el juego
        } catch (InterruptedException ex4) {
        }
        this.tablero.dispose(); // Cerrar el tablero
        try {
            this.entrada.close(); // Cerrar los flujos de entrada y salida
            this.salida.close();
            this.socketJugador.close(); // Cerrar el socket
        } catch (IOException ex5) {
        }
    }
}