package net.azarquiel.psp.backend;

import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HiloServidor extends Thread {

    private static int limite;
    private final int puerto;
    private final Compartido compartido;
    private List<Servidor> clientes;
    private Timer temporizador;

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
        this.clientes = new ArrayList<>();
        this.temporizador = new Timer();
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(puerto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DataOutputStream outputStreamJugador1 = null, outputStreamJugador2 = null;
        Servidor s1;
        Servidor s2;
        while (compartido.jugadores() < limite) {
            System.out.println("Esperando jugadores...");
            s1 = new Servidor(serverSocket, compartido);
            s2 = new Servidor(serverSocket, compartido);
            try {
                outputStreamJugador1 = s1.Conectar();
                System.out.println("Jugador 1 conectado");
                compartido.incrementa();

                outputStreamJugador2 = s2.Conectar();
                System.out.println("Jugador 2 conectado");
                compartido.incrementa();

                Servidor cliente = new Servidor(serverSocket, compartido);
                clientes.add(cliente);

                if (compartido.jugadores() == 1) {
                    temporizador.schedule(new EmparejamientoTask(outputStreamJugador1, outputStreamJugador2), 10000);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            s1.cargo(outputStreamJugador2);
            s2.cargo(outputStreamJugador1);
            try {
                outputStreamJugador1.writeInt(1);
                outputStreamJugador2.writeInt(0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            s1.start();
            s2.start();
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class EmparejamientoTask extends TimerTask {
        private final DataOutputStream outputStreamJugador1;
        private final DataOutputStream outputStreamJugador2;

        public EmparejamientoTask(DataOutputStream outputStreamJugador1, DataOutputStream outputStreamJugador2) {
            this.outputStreamJugador1 = outputStreamJugador1;
            this.outputStreamJugador2 = outputStreamJugador2;
        }

        @Override
        public void run() {
            if (clientes.size() == 2) {
                Servidor jugador1 = clientes.get(0);
                Servidor jugador2 = clientes.get(1);

                jugador1.cargo(outputStreamJugador2);
                jugador2.cargo(outputStreamJugador1);
                try {
                    outputStreamJugador1.writeInt(1);
                    outputStreamJugador2.writeInt(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                jugador1.start();
                jugador2.start();
            } else if (clientes.size() == 1) {
                // Lógica para un solo cliente conectado
            }
        }
    }
}
