package net.azarquiel.psp.backend;


import net.azarquiel.psp.game.Tablero;

import java.io.*;
import java.net.*;

public class EscribeCliente extends Thread {
    private final Tablero tablero;
    PrintWriter fsalida = null;

    public EscribeCliente(PrintWriter pfsalida, Tablero tablero) {
        fsalida = pfsalida;
        this.tablero = tablero;
        start();
    }

    public void run() {
        try {
            String cadena, eco = "";
            System.out.println("Introduce cadena: ");
            do {
                // formato de la cadena: "jugada 1-1 X"
                synchronized (tablero.compartido) {
                    if (tablero.compartido.getJugada().isEmpty()) {
                        tablero.compartido.wait();
                    }
                    cadena = tablero.compartido.getJugada();
                }
                fsalida.println("jugador ha hecho jugada: " + cadena);
                if (cadena.contains("jugada")) {
                    fsalida.println(cadena);
                }
            } while (!cadena.equals("*"));
        } catch (Exception e) {
        }
    }
}