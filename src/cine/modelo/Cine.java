package cine.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cine implements Serializable {

    private ArrayList<Sala> salas = new ArrayList<>();
    private ArrayList<Cliente> clientes = new ArrayList<>();
    private ArrayList<Entrada> entradas = new ArrayList<>();
    private List<Compra> compras = new ArrayList<>();

    public ArrayList<Sala> getSalas() {
        return salas;
    }

    public ArrayList<Cliente> getClientes() {
        return clientes;
    }

    public ArrayList<Entrada> getEntradas() {
        return entradas;
    }

    public List<Compra> getCompras() {
        return compras;
    }

    @Override
    public String toString() {
        return "Cine{" + "salas=" + salas + ", clientes=" + clientes + ", entradas=" + entradas + '}';
    }

}
