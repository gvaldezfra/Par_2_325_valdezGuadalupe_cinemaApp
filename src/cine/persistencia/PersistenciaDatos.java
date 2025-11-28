package cine.persistencia;

import cine.modelo.Cine;
import java.io.*;

public class PersistenciaDatos {

    private static final String ARCHIVO = "cine.ser";

    public static void guardar(Cine cine) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
            out.writeObject(cine);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static Cine cargar() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ARCHIVO))) {
            return (Cine) in.readObject();
        } catch (Exception e) {
            return new Cine(); // si no existe, crear nuevo
        }
    }
}
