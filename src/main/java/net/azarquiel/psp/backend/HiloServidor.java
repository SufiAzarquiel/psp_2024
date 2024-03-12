package net.azarquiel.psp.backend;

import java.io.*;
import java.net.ServerSocket;

public class HiloServidor extends Thread {

    private static int limite;
    private final int puerto;
    private final Compartido compartido;

    public static void main(String[] args) {
        if (args.length == 0 || args[0].isEmpty()) {
            System.out.println("Introduce el límite de jugadores.");
            System.exit(0);
        } else {
            limite = Integer.parseInt(args[0]);
            if (limite % 2 != 0) {
                System.out.println("El número de jugadores debe ser par.");
                System.exit(0);
            } else if (limite < 2) {
                System.out.println("El número de jugadores debe ser mayor que 1.");
                System.exit(0);
            } else if (limite > 100) {
                System.out.println("El número de jugadores debe ser menor que 100.");
                System.exit(0);
            }
        }
        Compartido c = new Compartido();
        new HiloServidor(3030, c);
    }

    public HiloServidor(int puerto, Compartido c) {
        this.start();
        this.compartido = c;
        this.puerto = puerto;
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(puerto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DataOutputStream fs1 = null, fs2 = null;
        Servidor s1;
        Servidor s2;
        while (compartido.jugadores() < limite) {
            System.out.println("Esperando jugadores...");
            s1 = new Servidor(serverSocket, compartido);
            s2 = new Servidor(serverSocket, compartido);
            // Me aseguro de que se conecten dos y solo dos
            try {
                fs1 = s1.Conectar();
                System.out.println("Jugador 1 conectado");

                fs2 = s2.Conectar();
                System.out.println("Jugador 2 conectado");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            // Cruzamos los streams de salida e informamos
            s1.cargo(fs2);
            s2.cargo(fs1);
            // Enviamos el turno al jugador 1
            try {
                fs1.writeInt(1); // turno del jugador 1
                fs2.writeInt(0); // jugador 2 espera
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // Activamos el funcionamiento cuando haya ya dos
            s1.start();
            s2.start();
            // Esperamos a que terminen y cerramos los sockets
            // s1.join();
            // s2.join();
            compartido.incrementa();
        }
        // Cerramos el servidor
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

