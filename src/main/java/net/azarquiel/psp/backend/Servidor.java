package net.azarquiel.psp.backend;

import java.io.*;
import java.net.*;

public class Servidor extends Thread {
    ServerSocket escucha;
    PrintWriter fsalida=null,fsalidaOtro=null;
    BufferedReader fentrada=null;
    Socket cliente=null;

    public Servidor(ServerSocket pescucha) {
        escucha=pescucha;
    }

    public void cargo(PrintWriter pfsalidaOtro){
        fsalidaOtro=pfsalidaOtro;
    }
    public PrintWriter Conectar() throws Exception{
        cliente=escucha.accept();
        fsalida=new PrintWriter(cliente.getOutputStream(),true);
        fentrada=new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        return fsalida;
    }

    public void run(){
        try {
            String cad=null;
            while ((cad=fentrada.readLine())!=null){
                fsalidaOtro.println(cad);
                if (cad.equals("*")) break;
            }
            fentrada.close();
            fsalida.close();
            cliente.close();
        } catch (Exception e){}
    }
}