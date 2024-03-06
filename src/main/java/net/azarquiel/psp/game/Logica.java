package net.azarquiel.psp.game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Logica extends Thread
{
    int jugador;
    Socket estelado;
    DataOutputStream salida;
    DataInputStream entrada;
    Tablero t;
    Posicion p;
    
    public Logica(final Tablero pt, final Posicion pp) {
        this.estelado = null;
        this.salida = null;
        this.entrada = null;
        this.t = pt;
        this.p = pp;
    }
    
    public void Conecto() {
        boolean termino = false;
        while (!termino) {
            termino = true;
            try {
                this.estelado = new Socket("psp.sufiazarquiel.com", 3030);
            }
            catch (IOException e) {
                termino = false;
                System.out.println(" No me puedo conectar");
            }
        }
        try {
            this.salida = new DataOutputStream(this.estelado.getOutputStream());
            this.entrada = new DataInputStream(this.estelado.getInputStream());
            
        }
        catch (IOException ex) {}
    }
    
    public int turno() {
        int aux = -1;
        try {
            aux = this.entrada.readInt();
        }
        catch (IOException ex) {}
        return aux;
    }
    
    public void inicio(final int pj) {
        this.jugador = pj;
        this.start();
    }
    
    @Override
    public void run() {
        boolean miturno = this.jugador == 1;
        int fila = 0;
        int columna = 0;
        while (this.t.hueco() && !this.t.enraya()) {
            if (miturno) {
                this.t.Activo();
                this.p.espera();
                try {
                    this.salida.writeInt(this.p.fila());
                    this.salida.writeInt(this.p.columna());
                }
                catch (IOException ex) {}
            }
            else {
                this.t.Desactivo();
                try {
                    fila = this.entrada.readInt();
                    columna = this.entrada.readInt();
                    this.t.Poner(fila, columna, this.p.otraletra());
                }
                catch (IOException ex2) {}
            }
            miturno = !miturno;
            try {
                Thread.sleep(500L);
            }
            catch (InterruptedException ex3) {}
        }
        if (this.t.enraya()) {
            this.t.gano();
        }
        try {
            Thread.sleep(2000L);
        }
        catch (InterruptedException ex4) {}
        this.t.dispose();
        try {
            this.entrada.close();
            this.salida.close();
            this.estelado.close();
        }
        catch (IOException ex5) {}
    }
}