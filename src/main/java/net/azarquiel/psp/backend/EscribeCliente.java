package net.azarquiel.psp.backend;


import net.azarquiel.psp.game.Tablero;

import java.io.*;
import java.net.*;

public class EscribeCliente extends Thread{
    private final Tablero tablero;
    PrintWriter fsalida=null;
    BufferedReader in;
    public EscribeCliente(PrintWriter pfsalida, Tablero tablero){
        fsalida=pfsalida;
        this.tablero=tablero;
        in=new BufferedReader(new InputStreamReader(System.in));
        start();
    }

    public void run(){
        try {
            String cadena,eco="";
            System.out.println("Introduce cadena: ");
            do{
                // formato de la cadena: "jugada 1-1 X"
                cadena=tablero.getJugada() == null ? "esperando" : tablero.getJugada();
                fsalida.println("jugador ha hecho jugada" + cadena);
                if (cadena.contains("jugada")) {
                    fsalida.println(cadena);
                }
                cadena = tablero.getJugada();
                tablero.setJugada("");
            } while (!cadena.equals("*"));
            in.close();
        } catch (Exception e){}
    }
}