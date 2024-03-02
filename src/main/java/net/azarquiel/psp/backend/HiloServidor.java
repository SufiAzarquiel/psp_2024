package net.azarquiel.psp.backend;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;

public class HiloServidor extends Thread {

    private final int puerto;

    public static void main(String[] args) {
        new HiloServidor(3030);
    }

    public HiloServidor(int puerto) {
        this.start();
        this.puerto = puerto;
    }

    @Override
    public void run() {
        ServerSocket serverSocket= null;
        try {
            serverSocket = new ServerSocket(puerto);
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
        PrintWriter fs1=null,fs2=null;
        Servidor s1;
        Servidor s2;
        while (true){
            s1=new Servidor(serverSocket);
            s2=new Servidor(serverSocket);
            // Me aseguro que se conecten dos y sólo dos
            try {
                fs1=s1.Conectar(); // dev stream de salida de jugador 1
                fs2=s2.Conectar(); // dev strean de salida de jugador 2
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            // cruzo los stream de salida e informo
            s1.cargo(fs2);
            s2.cargo(fs1);
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
