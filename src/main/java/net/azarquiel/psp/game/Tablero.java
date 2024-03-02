package net.azarquiel.psp.game;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;
import javax.swing.JFrame;

public class Tablero extends JFrame{

    final int n=3;
    JButton[][] boton;
    ActionListener accion;
    Font f;
    String jugada;
    public Tablero() {
        super("tres en raya");
        setSize(500,500);
        setResizable(false);
        f = new Font( Font.MONOSPACED, Font.PLAIN, 100);
        accion=new ActionListener() {
            public void actionPerformed(ActionEvent e){
                Poner((JButton)e.getSource(),"X");
            }
        };
        boton = new JButton[n][n];
        setLayout(new GridLayout(n, n));
        for(int i=0; i<n; i++)
            for(int j=0; j<n; j++){
                boton[i][j] = new JButton();
                boton[i][j].setActionCommand(i+"-"+j);
                boton[i][j].addActionListener(accion);
                boton[i][j].setFont(f);
                add(boton[i][j]);
            }
        repaint();
        setVisible(true);
    }

    public void Poner(int i,int j,String letra){
        boton[i][j].setText(letra);
        boton[i][j].setEnabled(false);
        repaint();
    }

    public void Poner(JButton j,String letra){
        j.setText(letra);
        j.setEnabled(false);
        j.repaint();
        // formato de la cadena: "jugada 1-1 X"
        jugada = "jugada " + j.getActionCommand() + " " + letra;
        System.out.println("nueva jugada: " + jugada);
    }

    public String getJugada(){
        return jugada;
    }

    public void setJugada(String jugada){
        this.jugada = jugada;
    }
}
