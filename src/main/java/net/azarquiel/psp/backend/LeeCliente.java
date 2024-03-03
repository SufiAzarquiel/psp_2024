package net.azarquiel.psp.backend;

import net.azarquiel.psp.game.Tablero;

import java.io.*;
import java.net.*;


public class LeeCliente extends Thread {
    BufferedReader fentrada = null;
    private final Tablero tablero;

    public LeeCliente(BufferedReader pfentrada, Tablero tablero) {
        fentrada = pfentrada;
        this.tablero = tablero;
        start();
    }

    public void run() {
        try {
            String cadena, eco = "";
            cadena = fentrada.readLine();
            while (!cadena.equals("*")) {
                // formato de la cadena: "jugada 1-1 X"
                if (cadena.contains("jugada")) {
                    // parsear la cadena y poner la jugada en el tablero
                    String[] jugada = cadena.split(" ");
                    String[] coordenadas = jugada[1].split("-");
                    int i = Integer.parseInt(coordenadas[0]);
                    int j = Integer.parseInt(coordenadas[1]);
                    String letra = jugada[2];
                    tablero.Poner(i, j, letra);
                    System.out.println("Jugada recibida: " + cadena);
                }
                cadena = fentrada.readLine();
            }
        } catch (Exception e) {
        }
    }
}
