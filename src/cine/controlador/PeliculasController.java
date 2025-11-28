package cine.controlador;

import cine.App;
import cine.modelo.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class PeliculasController {

    @FXML
    private ListView<HBox> listaPeliculas;
    @FXML
    private ListView<String> listaCompras;

    @FXML
    private ImageView imgPelicula;
    @FXML
    private Button btnComprar;

    private Cine cine = App.cine;
    private Sala salaSeleccionada = null;

    @FXML
    public void initialize() {
        cargarPeliculas();
        cargarComprasUsuario();
        configurarDobleClickCompras();

        btnComprar.setDisable(true);
    }

    // ====================================================
    //                 CARGAR PELÍCULAS
    // ====================================================
    private void cargarPeliculas() {

        for (Sala s : cine.getSalas()) {

            // Miniatura
            ImageView img = new ImageView();
            try {
                Image imagenMiniatura = new Image(
                        getClass().getResourceAsStream("/cine/resources/" + s.getPortada())
                );
                img.setImage(imagenMiniatura);
            } catch (NullPointerException e) {
                System.err.println("No se pudo cargar la imagen: " + s.getPortada());
            }

            img.setFitWidth(50);
            img.setFitHeight(60);

            Label lbl = new Label(s.getPelicula());

            HBox item = new HBox(10, img, lbl);
            item.setStyle("-fx-padding: 5;");

            // Click simple → muestra portada y habilita "Comprar"
            item.setOnMouseClicked(e -> {
                salaSeleccionada = s;
                App.setSalaActual(s);

                try {
                    Image imagenGrande = new Image(
                            getClass().getResourceAsStream("/cine/resources/" + s.getPortada())
                    );
                    imgPelicula.setImage(imagenGrande);
                } catch (NullPointerException ex) {
                    System.err.println("No se pudo cargar la imagen: " + s.getPortada());
                }

                btnComprar.setDisable(true);
                btnComprar.setDisable(false);
            });

            listaPeliculas.getItems().add(item);
        }
    }

    @FXML
    private void comprar() {
        if (salaSeleccionada == null) {
            return;
        }

        App.setSalaActual(salaSeleccionada);
        App.cambiarVentana("vista/fxml/ButacasView.fxml");
    }

    // ====================================================
    //               COMPRAS ANTERIORES
    // ====================================================
    private void cargarComprasUsuario() {

        Cliente cliente = App.getClienteActual();
        if (cliente == null) {
            return;
        }

        for (Entrada e : cine.getEntradas()) {
            if (e.getCliente().equals(cliente)) {

                String txt = "🎬 " + e.getSala().getPelicula()
                        + " | Sala " + e.getSala().getNumero()
                        + " | F" + e.getButaca().getFila()
                        + " A" + e.getButaca().getNumero();

                listaCompras.getItems().add(txt);
            }
        }
    }

    private void configurarDobleClickCompras() {

        listaCompras.setOnMouseClicked(evt -> {

            if (evt.getClickCount() == 2) {

                int index = listaCompras.getSelectionModel().getSelectedIndex();
                if (index < 0) {
                    return;
                }

                Cliente cliente = App.getClienteActual();
                int i = 0;

                for (Entrada e : cine.getEntradas()) {

                    if (e.getCliente().equals(cliente)) {

                        if (i == index) {
                            App.setEntradaActual(e);
                            App.cambiarVentana("vista/fxml/TicketView.fxml");
                            return;
                        }
                        i++;
                    }
                }
            }
        });
    }

    // ====================================================
    //                 CERRAR SESIÓN
    // ====================================================
    @FXML
    private void cerrarSesion() {

        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setHeaderText("Cerrar sesión");
        a.setContentText("¿Seguro que deseas cerrar sesión?");

        if (a.showAndWait().get() == ButtonType.OK) {
            App.setClienteActual(null);
            App.cambiarVentana("vista/fxml/LoginView.fxml");
        }
    }
}
