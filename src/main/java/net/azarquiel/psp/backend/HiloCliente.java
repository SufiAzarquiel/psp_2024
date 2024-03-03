package net.azarquiel.psp.backend;

import net.azarquiel.psp.game.Tablero;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HiloCliente extends Thread {
    private final String host;
    private final int puerto;
    private Tablero tablero;

    public HiloCliente(String host, int puerto, Tablero tablero) {
        this.host = host;
        this.puerto = puerto;
        this.tablero = tablero;
        this.start();
    }

    @Override
    public void run() {
        try {
            Socket servidor = new Socket(host, puerto);
            PrintWriter flujoSalida = null;
            flujoSalida = new PrintWriter(servidor.getOutputStream(), true);
            BufferedReader flujoEntrada = new BufferedReader(new InputStreamReader(servidor.getInputStream()));
            LeeCliente lc = new LeeCliente(flujoEntrada, tablero);
            EscribeCliente ec = new EscribeCliente(flujoSalida, tablero);
            lc.join();
            ec.join();
            flujoEntrada.close();
            flujoSalida.close();
            servidor.close();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
