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

    private final Cine cine = App.cine;
    private final List<Butaca> seleccionadas = new ArrayList<>();

    @FXML
    public void initialize() {

        System.out.println("[INIT] Iniciando carga de butacas...");

        try {
            Sala sala = App.getSalaActual();
            System.out.println("[INIT] Sala actual: " + sala.getPelicula());

            Butaca[][] but = sala.getButacas();
            System.out.println("[INIT] Butacas cargadas. Filas: " + but.length + ", Columnas: " + but[0].length);

            Label pantalla = new Label("PANTALLA");
            pantalla.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
            GridPane.setHalignment(pantalla, javafx.geometry.HPos.CENTER);
            gridButacas.add(pantalla, 0, 0, but[0].length + 1, 1);

            for (int c = 0; c < but[0].length; c++) {
                char letra = (char) ('A' + c);
                gridButacas.add(new Label(String.valueOf(letra)), c + 1, 1);
            }

            for (int f = 0; f < but.length; f++) {

                int filaReal = f + 1;
                gridButacas.add(new Label("Fila " + filaReal), 0, f + 2);

                for (int c = 0; c < but[0].length; c++) {

                    Butaca b = but[f][c];
                    Button btn = new Button(b.isOcupada() ? "X" : "O");

                    btn.setMinSize(35, 35);
                    btn.setStyle("-fx-background-color: " + (b.isOcupada() ? "red" : "lightgreen"));
                    btn.setDisable(b.isOcupada());

                    btn.setOnAction(e -> {
                        System.out.println("[CLICK] Butaca clickeada: " + b);

                        if (seleccionadas.contains(b)) {
                            seleccionadas.remove(b);
                            btn.setStyle("-fx-background-color: lightgreen");
                            System.out.println("[CLICK] Butaca deseleccionada.");
                        } else {
                            seleccionadas.add(b);
                            btn.setStyle("-fx-background-color: yellow");
                            System.out.println("[CLICK] Butaca seleccionada.");
                        }
                    });

                    gridButacas.add(btn, c + 1, f + 2);
                }
            }

            System.out.println("[INIT] Vista de butacas cargada correctamente.");

        } catch (Exception ex) {
            System.out.println("[ERROR-INIT] Error al cargar las butacas: " + ex.getMessage());
        }
    }

    @FXML
    private void confirmar() {

        System.out.println("[CONFIRMAR] Intentando confirmar compra...");

        try {
            Sala sala = App.getSalaActual();
            Cliente cliente = App.getClienteActual();

            System.out.println("[CONFIRMAR] Cliente actual: " + cliente.getNombre());
            System.out.println("[CONFIRMAR] Sala actual: " + sala.getPelicula());
            System.out.println("[CONFIRMAR] Butacas seleccionadas: " + seleccionadas.size());

            if (seleccionadas.isEmpty()) {
                System.out.println("[CONFIRMAR] No se seleccionaron butacas.");
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
                System.out.println("[CONFIRMAR] Usuario canceló la compra.");
                return;
            }

            System.out.println("[CONFIRMAR] Confirmación aceptada. Generando entradas...");

            List<Entrada> entradasCompra = new ArrayList<>();

            for (Butaca b : seleccionadas) {
                System.out.println("[CONFIRMAR] Ocupando butaca: " + b);
                b.ocupar();

                Entrada ent = new Entrada(cliente, sala, b);
                entradasCompra.add(ent);
                cine.getEntradas().add(ent);

                System.out.println("[CONFIRMAR] Entrada generada: " + ent);
            }

            int nuevoId = cine.getCompras().size() + 1;
            Compra compra = new Compra(nuevoId, cliente, sala, entradasCompra);
            cine.getCompras().add(compra);

            System.out.println("[CONFIRMAR] Compra creada con ID: " + nuevoId);

            PersistenciaDatos.guardar(cine);
            System.out.println("[CONFIRMAR] Persistencia guardada.");

            App.setCompraActual(compra);
            System.out.println("[CONFIRMAR] Redirigiendo a TicketView...");

            App.cambiarVentana("vista/fxml/TicketView.fxml");

        } catch (Exception ex) {
            System.out.println("[ERROR-CONFIRMAR] Error al confirmar compra: " + ex.getMessage());
        }
    }

    @FXML
    private void volver() {

        System.out.println("[VOLVER] Volviendo a vista de películas...");

        try {
            App.cambiarVentana("vista/fxml/PeliculasView.fxml");
            System.out.println("[VOLVER] Cambio de ventana realizado.");
        } catch (Exception ex) {
            System.out.println("[ERROR-VOLVER] Error al cambiar ventana: " + ex.getMessage());
        }
    }
}
