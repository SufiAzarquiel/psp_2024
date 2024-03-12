package net.azarquiel.psp.backend;
import java.io.*;
import java.net.*;

public class Servidor extends Thread {
    private final Compartido compartido;
    ServerSocket escucha;
    DataOutputStream fsalida = null, fsalidaOtro = null;
    DataInputStream fentrada = null;
    Socket cliente = null;

    public Servidor(ServerSocket pescucha, Compartido pcompartido) {
        this.compartido = pcompartido;
        escucha = pescucha;
    }

    public void cargo(DataOutputStream pfsalidaOtro) {
        fsalidaOtro = pfsalidaOtro;
    }

    public DataOutputStream Conectar() throws Exception {
        cliente = escucha.accept();
        fsalida = new DataOutputStream(cliente.getOutputStream());
        fentrada = new DataInputStream(cliente.getInputStream());
        return fsalida;
    }

    // Método para recibir el nombre del jugador
    public String RecibirNombre() throws IOException {
        return fentrada.readUTF();
    }

    public void run() {
        try {
            int cad = 0;
            while ((cad = fentrada.readInt()) != -1) {
                fsalidaOtro.writeInt(cad);
            }
            compartido.decrementa();
            fentrada.close();
            fsalida.close();
            cliente.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
