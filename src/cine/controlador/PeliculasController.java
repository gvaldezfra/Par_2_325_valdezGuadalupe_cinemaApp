package cine.controlador;

import cine.App;
import cine.modelo.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.*;

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

    private Map<Compra, String> mapaCompras = new HashMap<>();

    @FXML
    public void initialize() {
        cargarPeliculas();
        cargarComprasAgrupadas();
        configurarDobleClickCompras();

        btnComprar.setDisable(true);
    }

    private void cargarPeliculas() {

        for (Sala s : cine.getSalas()) {

            ImageView img = new ImageView();
            try {
                Image mini = new Image(
                        getClass().getResourceAsStream("/cine/resources/" + s.getPortada())
                );
                img.setImage(mini);
            } catch (Exception e) {
                System.err.println("No se pudo cargar la imagen: " + s.getPortada());
            }

            img.setFitWidth(50);
            img.setFitHeight(60);

            Label lbl = new Label(s.getPelicula());

            HBox item = new HBox(10, img, lbl);
            item.setStyle("-fx-padding: 5;");

            item.setOnMouseClicked(e -> {
                salaSeleccionada = s;
                App.setSalaActual(s);

                try {
                    Image grande = new Image(
                            getClass().getResourceAsStream("/cine/resources/" + s.getPortada())
                    );
                    imgPelicula.setImage(grande);
                } catch (Exception ex) {
                    System.err.println("No se pudo cargar la imagen: " + s.getPortada());
                }

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

    private void cargarComprasAgrupadas() {

        Cliente cliente = App.getClienteActual();
        if (cliente == null) {
            return;
        }

        listaCompras.getItems().clear();
        mapaCompras.clear();

        for (Compra compra : cine.getCompras()) {

            if (!compra.getCliente().equals(cliente)) {
                continue;
            }

            String titulo
                    = compra.getSala().getPelicula() + " — "
                    + compra.getEntradas().size() + " butaca(s)";

            listaCompras.getItems().add(titulo);
            mapaCompras.put(compra, titulo);
        }
    }

    private void configurarDobleClickCompras() {

        listaCompras.setOnMouseClicked(evt -> {

            if (evt.getClickCount() == 2) {

                String seleccion = listaCompras.getSelectionModel().getSelectedItem();
                if (seleccion == null) {
                    return;
                }

                // Encontrar la compra asociada
                Compra compraElegida = null;

                for (Map.Entry<Compra, String> entry : mapaCompras.entrySet()) {
                    if (entry.getValue().equals(seleccion)) {
                        compraElegida = entry.getKey();
                        break;
                    }
                }

                if (compraElegida == null) {
                    return;
                }

                // ENVIAR LA COMPRA COMPLETA AL TICKET
                App.setCompraActual(compraElegida);

                // ABRIR TICKET
                App.cambiarVentana("vista/fxml/TicketView.fxml");
            }
        });
    }

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
