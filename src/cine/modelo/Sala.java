package cine.modelo;

import java.io.Serializable;

public class Sala implements Serializable {

    private int numero;
    private String pelicula;
    private Butaca[][] butacas;
    private String portada; 

    public Sala(int numero, String pelicula, int filas, int columnas, String portada) {
        this.numero = numero;
        this.pelicula = pelicula;
        this.portada = portada;

        butacas = new Butaca[filas][columnas];
        for (int f = 0; f < filas; f++)
            for (int c = 0; c < columnas; c++)
                butacas[f][c] = new Butaca(f, c);
    }

    public int getNumero() { return numero; }
    public String getPelicula() { return pelicula; }
    public Butaca[][] getButacas() { return butacas; }

    public String getPortada() { return portada; }
    public void setPortada(String portada) { this.portada = portada; }

    @Override
    public String toString() {
        return "Sala{" + "numero=" + numero + ", pelicula=" + pelicula +
                ", portada=" + portada + '}';
    }
}
