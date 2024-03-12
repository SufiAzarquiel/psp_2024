package net.azarquiel.psp.game;

public class Timeout extends Thread {
    private long tiempoInicial;
    private final int segundos;

    public Timeout(int segundos) {
        this.segundos = segundos;
        this.start();
    }

    private boolean terminado = false;

    public synchronized boolean isTerminado() {
        return terminado;
    }

    @Override
    public void run() {
        tiempoInicial = System.currentTimeMillis();
        while (System.currentTimeMillis() - tiempoInicial < segundos * 1000) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                System.out.println("Error en el hilo de tiempo");
            }
        }
        terminado = true;
    }
}
