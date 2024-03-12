package net.azarquiel.psp.backend;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.Scanner;

public class HiloServidor extends Thread {

    private static int limite;
    private final int puerto;

    public static void main(String[] args) {
        if (args[0].isEmpty()) {
            System.out.println("Introduce limite de jugadores.");
            System.exit(0);
        } else{
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
        new HiloServidor(3030);
    }

    public HiloServidor(int puerto) {
        this.start();
        this.puerto = puerto;
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(puerto);
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
        PrintWriter fs1 = null, fs2 = null;
        Servidor s1;
        Servidor s2;
        int i = 0;
        while (i < limite) {
            System.out.println("Esperando jugadores...");
            s1 = new Servidor(serverSocket);
            s2 = new Servidor(serverSocket);
            //Me aseguro que se conecten dos y sólo dos
            try {
                fs1 = s1.Conectar();
                System.out.println("Jugador 1 conectado");

                fs2 = s2.Conectar();
                System.out.println("Jugador 2 conectado");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            // cruzo los stream de salida e informo
            s1.cargo(fs2);
            s2.cargo(fs1);
            // mando el turno al jugador 1
            fs1.println("1"); // turno del jugador 1
            fs2.println("0"); // jugador 2 espera
            // Activo el funcionamiento cuando haya ya dos
            // sería interesante almacenar los objetos servidor para posteriores intervenciones
            s1.start();
            s2.start();
            // espero a que terminen y cierro los sockets
            //s1.join();
            //s2.join();
        }
        //Demonio.close();
    }
}
