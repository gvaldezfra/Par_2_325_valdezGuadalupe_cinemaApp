package cine.modelo;
import java.io.Serializable;
import java.util.ArrayList;

public class Cine implements Serializable {

    private ArrayList<Sala> salas = new ArrayList<>();
    private ArrayList<Cliente> clientes = new ArrayList<>();
    private ArrayList<Entrada> entradas = new ArrayList<>();

    public ArrayList<Sala> getSalas() { return salas; }
    public ArrayList<Cliente> getClientes() { return clientes; }
    public ArrayList<Entrada> getEntradas() { return entradas; }

    @Override
    public String toString() {
        return "Cine{" + "salas=" + salas + ", clientes=" + clientes + ", entradas=" + entradas + '}';
    }
    
    
}
