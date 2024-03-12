package net.azarquiel.psp.game; // Define el paquete

import java.awt.*;
import java.awt.event.ActionEvent; // Importa ActionEvent para manejar eventos de acción
import java.awt.event.ActionListener; // Importa ActionListener para manejar acciones de los botones
import javax.swing.JButton; // Importa JButton para crear los botones del juego
import javax.swing.JFrame; // Importa JFrame para crear la ventana del juego

public class Tablero extends JFrame implements ActionListener // Define la clase Tablero que extiende JFrame e implementa ActionListener
{
    final int n = 3; // Define la dimensión del tablero como 3x3
    JButton[][] boton; // Matriz de botones que representan el tablero
    Font f; // Fuente para el texto en los botones
    boolean activo; // Booleano que indica si es el turno del jugador
    Posicion p; // Instancia de la clase Posicion para rastrear la posición de las jugadas

    // Constructor de la clase Tablero
    public Tablero(final Posicion pp) {
        super("tres en raya"); // Llama al constructor de la clase JFrame con el título "tres en raya"
        this.p = pp; // Inicializa la instancia de Posicion con el parámetro proporcionado
        this.setSize(500, 500); // Establece el tamaño de la ventana
        this.setResizable(false); // Evita que la ventana sea redimensionable
        this.activo = false; // Inicializa el estado activo como falso
        this.f = new Font("Monospaced", 0, 100); // Crea una nueva fuente para los botones
        this.boton = new JButton[3][3]; // Inicializa la matriz de botones
        this.setLayout(new GridLayout(3, 3)); // Establece el layout de la ventana como una cuadrícula de 3x3
        // Itera sobre la matriz de botones para crear y configurar cada botón
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                (this.boton[i][j] = new JButton()).setActionCommand(String.valueOf(i) + "-" + j); // Establece el comando de acción para cada botón
                this.boton[i][j].addActionListener(this); // Agrega un ActionListener a cada botón
                this.boton[i][j].setFont(this.f); // Establece la fuente para el texto del botón
                this.add(this.boton[i][j]); // Agrega el botón a la ventana
            }
        }
        this.repaint(); // Vuelve a pintar la ventana
        this.setVisible(true); // Hace visible la ventana
    }

    public void mostrarNombreJugador(String nombre) {
        this.setTitle("Turno de: " + nombre);
    }

    // Método para colocar una ficha en una posición específica del tablero
    public void Poner(final int i, final int j, final char letra) {
        this.boton[i][j].setText(new StringBuilder(String.valueOf(letra)).toString()); // Establece el texto del botón en la posición especificada
        this.boton[i][j].setActionCommand(i + "-" + j + "-" + "d"); // Establece el comando de acción del botón
        this.boton[i][j].setEnabled(false); // Deshabilita el botón para evitar más clics
        this.repaint(); // Vuelve a pintar la ventana
    }

    // Método sobrecargado para colocar una ficha en un botón específico
    public void Poner(final JButton j, final char letra) {
        j.setText(new StringBuilder(String.valueOf(letra)).toString()); // Establece el texto del botón proporcionado
        j.setEnabled(false); // Deshabilita el botón proporcionado
        j.repaint(); // Vuelve a pintar el botón
    }

    // Maneja los eventos de acción cuando se hace clic en un botón
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (this.activo) { // Si es el turno del jugador
            final String[] aux = e.getActionCommand().split("-"); // Divide el comando de acción en partes
            final int fila = Integer.parseInt(aux[0]); // Obtiene la fila del botón clicado
            final int columna = Integer.parseInt(aux[1]); // Obtiene la columna del botón clicado
            this.Poner((JButton)e.getSource(), this.p.letra()); // Coloca la ficha en el botón clicado
            ((JButton) e.getSource()).setActionCommand(fila + "-" + columna + "-" + "d"); // Actualiza el comando de acción del botón
            ((JButton) e.getSource()).setEnabled(false); // Deshabilita el botón clicado
            this.p.cargaPosicion(fila, columna); // Actualiza la posición del juego
            this.activo = false; // Cambia el estado activo a falso
            this.p.despierto(); // Despierta a la instancia de Posicion
        }
    }

    // Activa el tablero para el turno del jugador
    public void Activo() {
        this.setTitle("Turno de "  + p.nombreJugador()); // Establece el título de la ventana
        this.activo = true; // Cambia el estado activo a verdadero
        // Itera sobre los botones para habilitar los vacíos y deshabilitar los ocupados
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (this.boton[i][j].getActionCommand().split("-").length == 3) { // Si el botón está ocupado
                    this.boton[i][j].setEnabled(false); // Deshabilita el botón
                } else { // Si el botón está vacío
                    this.boton[i][j].setEnabled(true); // Habilita el botón
                }
            }
        }
    }

    // Desactiva el tablero mientras espera el turno del otro jugador
    public void Desactivo() {
        this.setTitle("Turno de "  + p.nombreOponente()); // Establece el título de la ventana
        this.activo = false; // Cambia el estado activo a falso
        // Itera sobre los botones para deshabilitarlos todos
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.boton[i][j].setEnabled(false); // Deshabilita el botón
            }
        }
    }

    // Indica que hay tres en raya
    public void gano() {
        this.setTitle
                (" HAY TRES EN RAYA "); // Establece el título de la ventana como " HAY TRES EN RAYA "
    }

    // Indica que el juego terminó en empate
    public void tablas() {
        this.setTitle("TABLAS"); // Establece el título de la ventana como "TABLAS"
    }

    // Verifica si hay algún espacio vacío en el tablero
    public boolean hueco() {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (this.boton[i][j].getText().equals("")) { // Si hay un botón vacío
                    return true; // Retorna verdadero
                }
            }
        }
        return false; // Retorna falso si no hay ningún botón vacío
    }

    // Verifica si hay una línea de fichas del mismo jugador en el tablero
    public boolean linea(final int x0, final int y0, final int x1, final int y1, final int x2, final int y2) {
        return !this.boton[x0][y0].getText().equals("") && // Retorna verdadero si los botones forman una línea y no están vacíos
                this.boton[x0][y0].getText().equals(this.boton[x1][y1].getText()) &&
                this.boton[x1][y1].getText().equals(this.boton[x2][y2].getText());
    }

    // Verifica si algún jugador ha ganado
    public boolean enraya() {
        // Verifica todas las combinaciones posibles de líneas ganadoras
        return  this.linea(0, 0, 0, 1, 0, 2) || // Horizontal superior
                this.linea(1, 0, 1, 1, 1, 2) || // Horizontal central
                this.linea(2, 0, 2, 1, 2, 2) || // Horizontal inferior
                this.linea(0, 0, 1, 0, 2, 0) || // Vertical izquierda
                this.linea(0, 1, 1, 1, 2, 1) || // Vertical central
                this.linea(0, 2, 1, 2, 2, 2) || // Vertical derecha
                this.linea(0, 0, 1, 1, 2, 2) || // Diagonal principal
                this.linea(0, 2, 1, 1, 2, 0); // Diagonal secundaria
    }
}

