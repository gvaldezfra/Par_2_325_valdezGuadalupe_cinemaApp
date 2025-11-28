package cine.modelo;
import java.io.Serializable;

public class Entrada implements Serializable {
    private Cliente cliente;
    private Sala sala;
    private Butaca butaca;

    public Entrada(Cliente cliente, Sala sala, Butaca butaca) {
        this.cliente = cliente;
        this.sala = sala;
        this.butaca = butaca;
    }

    public Cliente getCliente() { return cliente; }
    public Sala getSala() { return sala; }
    public Butaca getButaca() { return butaca; }

    @Override
    public String toString() {
        return "Entrada{" + "cliente=" + cliente + ", sala=" + sala + ", butaca=" + butaca + '}';
    }
    
    
}
