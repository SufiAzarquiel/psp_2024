package net.azarquiel.psp.backend; // Define el paquete

import java.io.*; // Importa las clases para entrada/salida
import java.net.ServerSocket; // Importa la clase ServerSocket para la comunicación del servidor

public class HiloServidor extends Thread { // Define la clase HiloServidor que extiende de Thread para la programación concurrente

    private static int limite; // Variable estática que almacena el límite de jugadores
    private final int puerto; // Puerto del servidor
    private final Compartido compartido; // Instancia de la clase Compartido para compartir datos entre hilos

    // Método principal para iniciar el servidor
    public static void main(String[] args) {
        // Verifica si se proporcionó el argumento para el límite de jugadores
        if (args.length == 0 || args[0].isEmpty()) {
            System.out.println("Introduce el límite de jugadores.");
            System.exit(0);
        } else {
            limite = Integer.parseInt(args[0]); // Obtiene el límite de jugadores desde el argumento
            // Verifica que el número de jugadores sea par, mayor que 1 y menor que 100
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
        Compartido c = new Compartido(); // Crea una instancia de Compartido
        new HiloServidor(3030, c); // Inicia un nuevo servidor en el puerto 3030 con la instancia de Compartido
    }

    // Constructor de la clase HiloServidor
    public HiloServidor(int puerto, Compartido c) {
        this.start(); // Inicia el hilo
        this.compartido = c; // Asigna la instancia de Compartido
        this.puerto = puerto; // Asigna el puerto
    }

    // Método run, que se ejecuta cuando se inicia el hilo
    @Override
    public void run() {
        ServerSocket serverSocket = null; // Declara un objeto ServerSocket
        try {
            serverSocket = new ServerSocket(puerto); // Crea un ServerSocket en el puerto especificado
        } catch (IOException e) {
            throw new RuntimeException(e); // Lanza una excepción si ocurre un error
        }
        DataOutputStream outputStreamJugador1 = null, outputStreamJugador2 = null; // Declara objetos DataOutputStream para los flujos de salida de los jugadores
        Servidor s1; // Declara un objeto Servidor para el jugador 1
        Servidor s2; // Declara un objeto Servidor para el jugador 2
        // Mientras el número de jugadores no alcance el límite
        while (compartido.jugadores() < limite) {
            System.out.println("Esperando jugadores..."); // Muestra un mensaje de espera
            s1 = new Servidor(serverSocket, compartido); // Crea un nuevo Servidor para el jugador 1
            s2 = new Servidor(serverSocket, compartido); // Crea un nuevo Servidor para el jugador 2
            // Se asegura de que se conecten dos y solo dos
            try {
                outputStreamJugador1 = s1.Conectar(); // Conecta al jugador 1
                System.out.println("Jugador 1 conectado"); // Muestra un mensaje de conexión exitosa para el jugador 1
                compartido.incrementa(); // Incrementa el contador de jugadores

                outputStreamJugador2 = s2.Conectar(); // Conecta al jugador 2
                System.out.println("Jugador 2 conectado"); // Muestra un mensaje de conexión exitosa para el jugador 2
                compartido.incrementa(); // Incrementa el contador de jugadores
            } catch (Exception e) {
                throw new RuntimeException(e); // Lanza una excepción si ocurre un error
            }
            // Cruza los flujos de salida e informa
            s1.cargo(outputStreamJugador2); // Asigna el flujo de salida del jugador 2 al jugador 1
            s2.cargo(outputStreamJugador1); // Asigna el flujo de salida del jugador 1 al jugador 2
            // Envía el turno al jugador 1
            try {
                outputStreamJugador1.writeInt(1); // Envía un 1 para indicar que es el turno del jugador 1
                outputStreamJugador2.writeInt(0); // Envía un 0 para indicar que el jugador 2 está en espera
            } catch (IOException e) {
                throw new RuntimeException(e); // Lanza una excepción si ocurre un error
            }
            // Activa el funcionamiento cuando ya hay dos jugadores
            s1.start(); // Inicia el hilo del servidor para el jugador 1
            s2.start(); // Inicia el hilo del servidor para el jugador 2
        }
        // Cierra el servidor cuando se alcanza el límite de jugadores
        try {
            serverSocket.close(); // Cierra el ServerSocket
        } catch (IOException e) {
            e.printStackTrace(); // Muestra una traza de la excepción si ocurre un error
        }
    }
}
