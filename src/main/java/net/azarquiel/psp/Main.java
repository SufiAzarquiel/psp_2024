package net.azarquiel.psp;

import net.azarquiel.psp.game.Logica;
import net.azarquiel.psp.game.Posicion;
import net.azarquiel.psp.game.Tablero;
import java.util.Scanner;

public class Main {
        public static void main(final String[] args) {
                // Creación de objetos para gestionar el juego
                final Posicion p = new Posicion(); // Se crea una instancia de la clase Posicion para manejar la posición del juego
                final Tablero t = new Tablero(p); // Se crea una instancia de la clase Tablero para mostrar el tablero del juego, utilizando la posición creada anteriormente
                final Logica logica = new Logica(t, p); // Se crea una instancia de la clase Logica para controlar la lógica del juego, pasando el tablero y la posición como parámetros

                // Conexión con el servidor
                logica.Conecto(); // Se establece la conexión con el servidor utilizando el método Conecto() de la instancia de Logica

                // Determinación del turno
                final int turno = logica.turno(); // Se determina el turno del jugador utilizando el método turno() de la instancia de Logica

                // Solicitud del nombre del jugador
                Scanner scanner = new Scanner(System.in); // Se crea un objeto Scanner para leer la entrada del usuario desde la consola
                System.out.println("Introduce tu nombre:"); // Se muestra un mensaje solicitando al jugador que introduzca su nombre
                String nombreJugador = scanner.nextLine(); // Se lee el nombre ingresado por el usuario y se almacena en la variable nombreJugador
                p.cargaNombreJugador(nombreJugador); // Se carga el nombre del jugador en la posición utilizando el método cargaNombreJugador() de la instancia de Posicion

                // Envío del nombre al servidor
                logica.enviarNombre(nombreJugador); // Se envía el nombre del jugador al servidor utilizando el método enviarNombre() de la instancia de Logica

                // Recepción del nombre del otro jugador y configuración del juego
                String nombreOtroJugador = logica.recibirNombre(); // Se recibe el nombre del otro jugador desde el servidor utilizando el método recibirNombre() de la instancia de Logica
                p.cargaNombreOponente(nombreOtroJugador); // Se carga el nombre del otro jugador en la posición utilizando el método cargaNombreOponente() de la instancia de Posicion
                // Configuración del turno y la letra del jugador en la posición
                p.cargaLetra((turno == 1) ? 'X' : 'O'); // Se determina la letra del jugador (X u O) según el turno y se carga en la posición
                logica.inicio(turno); // Se inicia el juego con el turno determinado utilizando el método inicio() de la instancia de Logica
        }
}
