package net.azarquiel.psp.backend; // Define el paquete

import java.io.*; // Importa las clases para entrada/salida
import java.net.*; // Importa las clases para comunicación de red

public class Servidor extends Thread { // Define la clase Servidor que extiende de Thread para la programación concurrente
    private final Compartido compartido; // Instancia de la clase Compartido
    ServerSocket escucha; // Socket del servidor para aceptar conexiones
    DataOutputStream fsalida = null, fsalidaOtro = null; // Flujos de salida para enviar datos
    DataInputStream fentrada = null; // Flujo de entrada para recibir datos
    Socket cliente = null; // Socket del cliente
    String[][] tablero;

    // Constructor de la clase Servidor
    public Servidor(ServerSocket pescucha, Compartido pcompartido) {
        this.compartido = pcompartido; // Inicializa la instancia de Compartido con el parámetro proporcionado
        escucha = pescucha;// Inicializa el ServerSocket con el parámetro proporcionado
        tablero = new String[3][3];
    }

    // Método para establecer la salida hacia el otro cliente
    public void cargo(DataOutputStream pfsalidaOtro) {
        fsalidaOtro = pfsalidaOtro; // Asigna el flujo de salida al otro cliente con el parámetro proporcionado
    }

    // Método para establecer la conexión con un cliente
    public DataOutputStream Conectar() throws Exception {
        cliente = escucha.accept(); // Espera y acepta la conexión de un cliente
        fsalida = new DataOutputStream(cliente.getOutputStream()); // Crea un flujo de salida hacia el cliente
        fentrada = new DataInputStream(cliente.getInputStream()); // Crea un flujo de entrada desde el cliente
        return fsalida; // Retorna el flujo de salida
    }

    // Método para recibir el nombre del jugador
    public String RecibirNombre() throws IOException {
        return fentrada.readUTF(); // Lee y retorna el nombre del jugador desde el flujo de entrada
    }

    // Método run, que se ejecuta cuando se inicia el hilo
    public void run() {
        try {
            String nombre = RecibirNombre(); // Recibe el nombre del jugador
            fsalidaOtro.writeUTF(nombre); // Envía el nombre del jugador al otro cliente
            int cad = 0; // Variable para almacenar datos recibidos
            while ((cad = fentrada.readInt()) != -1) { // Mientras haya datos en el flujo de entrada
                fsalidaOtro.writeInt(cad); // Envía los datos al otro cliente
            }
            compartido.decrementa(); // Decrementa el contador compartido
            fentrada.close(); // Cierra el flujo de entrada
            fsalida.close(); // Cierra el flujo de salida
            cliente.close(); // Cierra el socket del cliente
        } catch (Exception e) {
            e.printStackTrace(); // Imprime la traza de la excepción
        }
    }
    public void recibirPosicionesDelTablero() {
        try {
            ServerSocket serverSocket = new ServerSocket(3030); // Crea un ServerSocket en el puerto 1234
            Socket socket = serverSocket.accept(); // Espera a que un cliente se conecte
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            // Recibe las posiciones del tablero del cliente
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    int posicion = dataInputStream.readInt(); // Lee la posición del tablero
                    // Haz lo que necesites con la posición recibida
                }
            }

            dataInputStream.close(); // Cierra el flujo de entrada
            socket.close(); // Cierra el socket
            serverSocket.close(); // Cierra el ServerSocket
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void jugarMaquina() {
        // Posición aleatoria en el tablero y realizar un movimiento en esa posición
        int fila = (int) (Math.random() * tablero.length);
        int columna = (int) (Math.random() * tablero[0].length);
        // Realiza el movimiento en la posición aleatoria
        // Aquí asumimos que tablero es un array bidimensional que representa el tablero del juego
        tablero[fila][columna] = "X"; // Por ejemplo, marca la posición con una 'X'
    }
}
