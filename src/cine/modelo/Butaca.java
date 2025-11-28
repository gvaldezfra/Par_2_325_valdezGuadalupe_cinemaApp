package cine.modelo;
import java.io.Serializable;

public class Butaca implements Serializable {
    private int fila;
    private int numero;     // esta es la columna realmente
    private boolean ocupada;

    public Butaca(int fila, int numero) {
        this.fila = fila;
        this.numero = numero;
        this.ocupada = false;
    }

    public int getFila() { 
        return fila; 
    }

    public int getNumero() { 
        return numero; 
    }

    // Alias para compatibilidad con código que usa "columna"
    public int getColumna() { 
        return numero; 
    }

    public boolean isOcupada() { 
        return ocupada; 
    }

    public void ocupar() { 
        this.ocupada = true; 
    }

    @Override
    public String toString() {
        return "Butaca{" + "fila=" + fila + ", numero=" + numero + ", ocupada=" + ocupada + '}';
    }
}
