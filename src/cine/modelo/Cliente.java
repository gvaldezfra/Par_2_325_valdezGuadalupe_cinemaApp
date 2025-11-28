package cine.modelo;
import java.io.Serializable;

public class Cliente implements Serializable {
    private String nombre;
    private String email;
    private String password;

    public Cliente(String nombre, String email, String password) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
    }

    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }

    @Override
    public String toString() {
        return "Cliente{" + "nombre=" + nombre + ", email=" + email + ", password=" + password + '}';
    }
    
    
}
