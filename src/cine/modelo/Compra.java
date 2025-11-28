package cine.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Compra implements Serializable {

    private int id;
    private Cliente cliente;
    private Sala sala;
    private List<Entrada> entradas;
    private LocalDateTime fecha;

    public Compra(int id, Cliente cliente, Sala sala, List<Entrada> entradas) {
        this.id = id;
        this.cliente = cliente;
        this.sala = sala;
        this.entradas = entradas;
        this.fecha = LocalDateTime.now();
    }

    public int getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public Sala getSala() { return sala; }
    public List<Entrada> getEntradas() { return entradas; }
    public LocalDateTime getFecha() { return fecha; }

    public int getCantidad() {
        return entradas.size();
    }
}
