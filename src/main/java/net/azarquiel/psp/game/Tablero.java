package net.azarquiel.psp.game;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Tablero extends JFrame implements ActionListener
{
    final int n = 3;
    JButton[][] boton;
    Font f;
    boolean activo;
    Posicion p;
    
    public Tablero(final Posicion pp) {
        super("tres en raya");
        this.p = pp;
        this.setSize(500, 500);
        this.setResizable(false);
        this.activo = false;
        this.f = new Font("Monospaced", 0, 100);
        this.boton = new JButton[3][3];
        this.setLayout(new GridLayout(3, 3));
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                (this.boton[i][j] = new JButton()).setActionCommand(String.valueOf(i) + "-" + j);
                this.boton[i][j].addActionListener(this);
                this.boton[i][j].setFont(this.f);
                this.add(this.boton[i][j]);
            }
        }
        this.repaint();
        this.setVisible(true);
    }
    
    public void Poner(final int i, final int j, final char letra) {
        this.boton[i][j].setText(new StringBuilder(String.valueOf(letra)).toString());
        this.boton[i][j].setEnabled(false);
        this.repaint();
    }
    
    public void Poner(final JButton j, final char letra) {
        j.setText(new StringBuilder(String.valueOf(letra)).toString());
        j.setEnabled(false);
        j.repaint();
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (this.activo) {
            final String[] aux = e.getActionCommand().split("-");
            final int fila = Integer.parseInt(aux[0]);
            final int columna = Integer.parseInt(aux[1]);
            this.Poner((JButton)e.getSource(), this.p.letra());
            this.p.cargaPosicion(fila, columna);
            this.activo = false;
            this.p.despierto();
        }
    }

    public void Activo() {
        this.setTitle("Es tu turno");
        this.activo = true;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.boton[i][j].setEnabled(true);
            }
        }
    }

    public void Desactivo() {
        this.setTitle("Espera a que el otro juegu");
        this.activo = false;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.boton[i][j].setEnabled(false);
            }
        }
    }
    
    public void gano() {
        this.setTitle(" HAY TRES EN RAYA ");
    }
    
    public boolean hueco() {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (this.boton[i][j].getText().equals("")) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean linea(final int x0, final int y0, final int x1, final int y1, final int x2, final int y2) {
        return !this.boton[x0][y0].getText().equals("") &&
                this.boton[x0][y0].getText().equals(this.boton[x1][y1].getText()) &&
                this.boton[x1][y1].getText().equals(this.boton[x2][y2].getText());
    }
    
    public boolean enraya() {
        return  this.linea(0, 0, 0, 1, 0, 2) ||
                this.linea(1, 0, 1, 1, 1, 2) ||
                this.linea(2, 0, 2, 1, 2, 2) ||
                this.linea(0, 0, 1, 0, 2, 0) ||
                this.linea(0, 1, 1, 1, 2, 1) ||
                this.linea(0, 2, 1, 2, 2, 2) ||
                this.linea(0, 0, 1, 1, 2, 2) ||
                this.linea(0, 2, 1, 1, 2, 0);
    }
}