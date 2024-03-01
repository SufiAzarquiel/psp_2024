package net.azarquiel.psp;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        String Host="psp.sufiazarquiel.com";
        int puerto=3030;

        // Crea socket si alguien escucha
        Socket socketCliente = null;

        // crear socket con try-with-resources
        try (Socket socket = new Socket(Host, puerto)) {

            socketCliente = socket;
            PrintWriter out=new PrintWriter(socketCliente.getOutputStream());

            // quitar delay para tener respuestas rapidas
            socket.setTcpNoDelay(true);
            // mandar mensaje
            out.println("hola");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}