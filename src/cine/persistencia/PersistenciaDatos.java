package cine.persistencia;

import cine.modelo.Cine;
import java.io.*;

public class PersistenciaDatos {

    private static final String CARPETA = "data";
    private static final String ARCHIVO = CARPETA + File.separator + "cine.ser";

    static {
        File f = new File(CARPETA);
        if (!f.exists()) f.mkdirs();
    }

    public static void guardar(Cine cine) {
        File archivoReal = new File(ARCHIVO);
        File archivoTemp = new File(ARCHIVO + ".tmp");

        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(archivoTemp))) {

            out.writeObject(cine);
            out.flush();

            if (archivoReal.exists()) archivoReal.delete();
            archivoTemp.renameTo(archivoReal);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Cine cargar() {
        File archivo = new File(ARCHIVO);

        if (!archivo.exists()) {
            return new Cine();
        }

        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(archivo))) {

            return (Cine) in.readObject();

        } catch (Exception e) {
            e.printStackTrace();
            return new Cine();
        }
    }
}
