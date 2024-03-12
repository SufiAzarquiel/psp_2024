package net.azarquiel.psp;

import net.azarquiel.psp.game.Logica;
import net.azarquiel.psp.game.Posicion;
import net.azarquiel.psp.game.Tablero;

public class Main {
	public static void main(final String[] args) {
        final Posicion p = new Posicion();
        final Tablero t = new Tablero(p);
        final Logica logica = new Logica(t, p);
        logica.Conecto();
        final int turno = logica.turno();

        // leer nombre por consola
        // enviar nombre al servidor
        // recibir nombre del otro jugador y meterlo en el tablero
        p.cargaLetra((turno == 1) ? 'X' : 'O');
        logica.inicio(turno);
    }
}
