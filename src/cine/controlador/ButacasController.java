package cine.controlador;

import cine.App;
import cine.modelo.*;
import cine.persistencia.PersistenciaDatos;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;
import java.util.List;

public class ButacasController {

    @FXML
    private GridPane gridButacas;

    private Cine cine = App.cine;
    private List<Butaca> seleccionadas = new ArrayList<>();

    @FXML
    public void initialize() {

        Sala sala = App.getSalaActual();
        Butaca[][] but = sala.getButacas();

        for (int f = 0; f < but.length; f++) {
            for (int c = 0; c < but[0].length; c++) {

                Butaca b = but[f][c];
                Button btn = new Button(b.isOcupada() ? "X" : "O");

                btn.setMinSize(35, 35);
                btn.setStyle("-fx-background-color: " + (b.isOcupada() ? "red" : "lightgreen"));
                btn.setDisable(b.isOcupada());

                btn.setOnAction(e -> {
                    if (seleccionadas.contains(b)) {
                        seleccionadas.remove(b);
                        btn.setStyle("-fx-background-color: lightgreen");
                    } else {
                        seleccionadas.add(b);
                        btn.setStyle("-fx-background-color: yellow");
                    }
                });

                gridButacas.add(btn, c, f);
            }
        }
    }

    @FXML
    private void confirmar() {

        Sala sala = App.getSalaActual();
        Cliente cliente = App.getClienteActual();

        if (seleccionadas.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("No seleccionaste ninguna butaca");
            a.setContentText("Debes elegir al menos una butaca.");
            a.showAndWait();
            return;
        }

        Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
        conf.setHeaderText("Confirmar compra");
        conf.setContentText("¿Deseas comprar " + seleccionadas.size() + " butaca(s)?");

        if (conf.showAndWait().get() != ButtonType.OK) {
            return;
        }

        List<Entrada> entradasCompra = new ArrayList<>();

        for (Butaca b : seleccionadas) {
            b.ocupar();

            Entrada ent = new Entrada(cliente, sala, b);
            cine.getEntradas().add(ent);
            entradasCompra.add(ent);
        }

        PersistenciaDatos.guardar(cine);

        App.setEntradasActuales(entradasCompra);
        App.cambiarVentana("vista/fxml/TicketView.fxml");
    }

    @FXML
    private void volver() {
        App.cambiarVentana("vista/fxml/PeliculasView.fxml");
    }
}
