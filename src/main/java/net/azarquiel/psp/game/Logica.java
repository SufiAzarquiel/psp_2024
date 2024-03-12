package net.azarquiel.psp.game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Logica extends Thread
{
    int jugador;
    Socket socketJugador;
    DataOutputStream salida;
    DataInputStream entrada;
    Tablero tablero;
    Posicion compartidoPos;
    
    public Logica(final Tablero pt, final Posicion pp) {
        this.socketJugador = null;
        this.salida = null;
        this.entrada = null;
        this.tablero = pt;
        this.compartidoPos = pp;
    }
    
    public void Conecto() {
        String host = "nube1.sufiazarquiel.com";
        int puerto = 3030;
        boolean termino = false;
        while (!termino) {
            termino = true;
            try {
                this.socketJugador = new Socket(host, puerto);
            }
            catch (IOException e) {
                termino = false;
                System.out.printf("Error al conectar con el servidor por el puerto: %d.\nHost: %s\n", puerto, host);
            }
        }
        try {
            this.salida = new DataOutputStream(this.socketJugador.getOutputStream());
            this.entrada = new DataInputStream(this.socketJugador.getInputStream());
            
        }
        catch (IOException ex) {
            System.out.println("Error al crear los flujos de entrada y salida");
        }
    }
    
    public int turno() {
        int turno = -1;
        try {
            turno = this.entrada.readInt();
        }
        catch (IOException ex) {
            System.out.println("Error al leer turno");
        }
        return turno;
    }
    
    public void inicio(final int pj) {
        this.jugador = pj;
        this.start();
    }

    @Override
    public void run() {
        boolean esMiTurno = this.jugador == 1;
        int fila = 0;
        int columna = 0;
        while (this.tablero.hueco() && !this.tablero.enraya()) {
            // if (System.currentTimeMillis() - inicioTurno >= 2 * 60 * 1000) {
            //            esMiTurno = !esMiTurno; // Cambiar el turno al otro jugador
            //            inicioTurno = System.currentTimeMillis(); // Actualizar el tiempo de inicio del turno
            //            // Notificar al otro jugador que es su turno ahora
            //            if (esMiTurno) {
            //                this.tablero.Desactivo(); // Desactivar el tablero para el jugador actual
            //                this.salida.writeInt(1); // 1 indica que es el turno del jugador 1
            //            } else {
            //                this.tablero.Activo(); // Activar el tablero para el otro jugador
            //                // Indicar al jugador que espere ya que el otro tardó demasiado
            //                this.salida.writeInt(0); // 0 indica que el jugador debe esperar
            //            }
            //            // Saltar al siguiente ciclo del bucle para esperar la respuesta del otro jugador
            //            continue;
            //        }
            if (esMiTurno) {
                this.tablero.Activo();
                this.compartidoPos.espera();
                try {
                    this.salida.writeInt(this.compartidoPos.fila());
                    this.salida.writeInt(this.compartidoPos.columna());

                }
                catch (IOException ex) {
                    System.out.println("Error al comunicar nueva posicion al tablero");
                }
            }
            else {
                this.tablero.Desactivo();
                try {
                    fila = this.entrada.readInt();
                    columna = this.entrada.readInt();
                    this.tablero.Poner(fila, columna, this.compartidoPos.otraletra());
                }
                catch (IOException ex2) {
                    System.out.println("Error al leer nueva posicion del tablero");
                }
            }
            esMiTurno = !esMiTurno;
            try {
                Thread.sleep(500L);
            }
            catch (InterruptedException ex3) {
                System.out.println("Hilo de logica interrumpido");
            }

        }
        if (!this.tablero.hueco()) {
            this.tablero.tablas(); // Si no hay hueco, quedan en tabla
        }

        if (this.tablero.enraya()) {
            this.tablero.gano();
        }
        try {
            Thread.sleep(2000L);
        }
        catch (InterruptedException ex4) {}
        this.tablero.dispose();
        try {
            this.entrada.close();
            this.salida.close();
            this.socketJugador.close();
        }
        catch (IOException ex5) {}
    }
    //Metodo run modificado para que cuando pasen 2 minutos sin responder pase de turno
    /*
    public void run() {
    boolean miturno = this.jugador == 1;
    long inicioTurno = System.currentTimeMillis();
    int fila = 0;
    int columna = 0;
    while (this.t.hueco() && !this.t.enraya()) {
        // Verificar si ha pasado 2 minutos desde el inicio del turno
        if (System.currentTimeMillis() - inicioTurno >= 2 * 60 * 1000) {
            miturno = !miturno; // Cambiar el turno al otro jugador
            inicioTurno = System.currentTimeMillis(); // Actualizar el tiempo turno
            // Notificar al otro jugador que es su turno ahora
            if (miturno) {
                this.t.Desactivo(); // Desactivar el tablero para el jugador actual
                this.salida.writeInt(1); // 1 indica que es el turno del jugador 1
            } else {
                this.t.Activo(); // Activar el tablero para el otro jugador
                // Indicar al jugador que espere ya que el otro tardó demasiado
                this.salida.writeInt(0); // 0 indica que el jugador debe esperar
            }
            // Saltar al siguiente ciclo del bucle para esperar la respuesta del otro jugador
            continue;
        }

        if (miturno) {
            this.t.Activo();
            this.p.espera();
            try {
                this.salida.writeInt(this.p.fila());
                this.salida.writeInt(this.p.columna());
            } catch (IOException ex) {
                // Manejar la excepción
            }
        } else {
            this.t.Desactivo();
            try {
                fila = this.entrada.readInt();
                columna = this.entrada.readInt();
                this.t.Poner(fila, columna, this.p.otraletra());
            } catch (IOException ex2) {
                // Manejar la excepción
            }
        }
        miturno = !miturno;
        try {
            Thread.sleep(500L);
        } catch (InterruptedException ex3) {
            // Manejar la excepción
        }
    }
    if (this.t.enraya()) {
        this.t.gano();
    }
    try {
        Thread.sleep(2000L);
    } catch (InterruptedException ex4) {
        // Manejar la excepción
    }
    this.t.dispose();
    try {
        this.entrada.close();
        this.salida.close();
        this.estelado.close();
    } catch (IOException ex5) {
        // Manejar la excepción
    }
}

    */
}