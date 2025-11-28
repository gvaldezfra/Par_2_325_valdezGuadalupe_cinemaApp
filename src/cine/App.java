package cine;

import cine.modelo.Cine;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import cine.modelo.Cliente;
import cine.modelo.Compra;
import cine.modelo.Sala;
import cine.modelo.Entrada;
import cine.persistencia.PersistenciaDatos;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class App extends Application {

    private static Scene scene;
    public static Stage stage;

    public static Cine cine = PersistenciaDatos.cargar();

    private static Cliente clienteActual;
    private static Sala salaActual;
    private static Entrada entradaActual;
    private static List<Entrada> entradasActuales = new ArrayList<>();
    private static Compra compraActual;

    @Override
    public void start(Stage stage) throws Exception {

        App.stage = stage;

        inicializarDatos();

        Parent root = loadFXML("vista/fxml/LoginView.fxml");

        scene = new Scene(root, 900, 600);
        stage.setScene(scene);

        stage.setTitle("Cine");
        stage.setResizable(false);
        stage.show();
    }

    public static void cambiarVentana(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml));
            Parent nuevoRoot = loader.load();

            Scene sceneActual = stage.getScene();

            if (sceneActual == null) {
                stage.setScene(new Scene(nuevoRoot));
                stage.show();
                return;
            }

            FadeTransition fadeOut = new FadeTransition(Duration.millis(250), sceneActual.getRoot());
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            fadeOut.setOnFinished(e -> {
                sceneActual.setRoot(nuevoRoot);

                FadeTransition fadeIn = new FadeTransition(Duration.millis(250), nuevoRoot);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });

            fadeOut.play();

        } catch (Exception e) {
            System.out.println("❌ ERROR cargando FXML: " + fxml);
            e.printStackTrace();
        }
    }

    private static Parent loadFXML(String ruta) {
        try {
            return new FXMLLoader(App.class.getResource("/cine/" + ruta)).load();
        } catch (Exception e) {
            System.out.println("\n❌ ERROR cargando FXML: " + ruta);
            e.printStackTrace();
            return null;
        }
    }

    // ==== GETTERS / SETTERS ====
    public static void setClienteActual(Cliente c) {
        clienteActual = c;
    }

    public static Cliente getClienteActual() {
        return clienteActual;
    }

    public static void setSalaActual(Sala s) {
        salaActual = s;
    }

    public static Sala getSalaActual() {
        return salaActual;
    }

    public static void setEntradaActual(Entrada e) {
        entradaActual = e;
    }

    public static Entrada getEntradaActual() {
        return entradaActual;
    }

    public static void setEntradasActuales(List<Entrada> lista) {
        entradasActuales = lista;
    }

    public static List<Entrada> getEntradasActuales() {
        return entradasActuales;
    }

    public static void setCompraActual(Compra c) {
        compraActual = c;
    }

    public static Compra getCompraActual() {
        return compraActual;
    }

    public static void main(String[] args) {
        launch();
    }

    private void inicializarDatos() {

        // Usamos SIEMPRE el cine global
        if (cine.getSalas().isEmpty()) {

            cine.getSalas().add(new Sala(
                    1, "Avengers: Endgame", 5, 8,
                    "avengers_end_game.jpg"
            ));

            cine.getSalas().add(new Sala(
                    2, "Avatar 2", 5, 8,
                    "avatar_2_el_camino_del_agua.jpeg"
            ));

            cine.getSalas().add(new Sala(
                    3, "Mario Bros", 5, 8,
                    "mario.jpg"
            ));

            cine.getSalas().add(new Sala(
                    4, "Spider-Man: No Way Home", 5, 8,
                    "spiderman.jpg"
            ));

            PersistenciaDatos.guardar(cine);
        }
    }
}
