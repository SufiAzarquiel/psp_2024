package net.azarquiel.psp;


import net.azarquiel.psp.backend.HiloCliente;
import net.azarquiel.psp.backend.HiloServidor;
import net.azarquiel.psp.backend.Servidor;
import net.azarquiel.psp.game.Tablero;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        // variables de conexion
        int puerto = 3030;
        String host = "psp.sufiazarquiel.com";

        // abrir la ventana de juego
        Tablero tablero = new Tablero();

        // conectarse al servidor
        // recibir respuesta: emparejado o esperando
        // si esperando, esperar a que se empareje
        HiloCliente cliente = new HiloCliente(host, puerto, tablero);

        // enviar el nombre del jugador
        // si es tu turno:
        // - cada vez que se haga un movimiento, enviarlo al servidor
        // si no es tu turno:
        // - esperar a que el oponente haga un movimiento
        // recibir respuesta: ganador, empate o seguir jugando

        // si emparejado, recibir:
        // - el nombre del oponente
        // - el turno (true o false) -> hilo
        // - el tablero -> hilo
    }
}