package net.azarquiel.psp;

import net.azarquiel.psp.game.Logica;
import net.azarquiel.psp.game.Posicion;
import net.azarquiel.psp.game.Tablero;
import java.util.Scanner;

public class Main {
	public static void main(final String[] args) {
        final Posicion p = new Posicion();
        final Tablero t = new Tablero(p);
        final Logica logica = new Logica(t, p);
        logica.Conecto();
        final int turno = logica.turno();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Introduce tu nombre:");
        String nombreJugador = scanner.nextLine();
        p.cargaNombreJugador(nombreJugador);

        // Enviar nombre al servidor
        logica.enviarNombre(nombreJugador);

        // Recibir nombre del otro jugador y mostrarlo en el tablero
        String nombreOtroJugador = logica.recibirNombre();
        p.cargaNombreOponente(nombreOtroJugador);
        // leer nombre por consola
        // enviar nombre al servidor
        // recibir nombre del otro jugador y meterlo en el tablero
        p.cargaLetra((turno == 1) ? 'X' : 'O');
        logica.inicio(turno);
    }
}
