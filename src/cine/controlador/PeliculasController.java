package cine.controlador;

import cine.App;
import cine.modelo.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.HashMap;
import java.util.Map;

public class PeliculasController {

    @FXML private ListView<HBox> listaPeliculas;
    @FXML private ListView<String> listaCompras;
    @FXML private ImageView imgPelicula;
    @FXML private Button btnComprar;

    private Cine cine = App.cine;
    private Sala salaSeleccionada = null;

    private Map<HBox, Sala> hboxToSala = new HashMap<>();

    @FXML
    public void initialize() {

        System.out.println("[INIT] Iniciando PeliculasController...");
        try {

            btnComprar.setDisable(true);

            System.out.println("[INIT] Cargando películas...");
            cargarPeliculas();

            System.out.println("[INIT] Cargando compras agrupadas...");
            cargarComprasAgrupadas();

            System.out.println("[INIT] Configurando doble click en compras...");
            configurarDobleClickCompras();

            System.out.println("[INIT] Configurando selección de películas...");
            configurarSeleccionListaPeliculas();

            System.out.println("[INIT] PeliculasController inicializado correctamente.");

        } catch (Exception ex) {
            System.out.println("[ERROR-INIT] Error en initialize(): " + ex.getMessage());
        }
    }

    private void cargarPeliculas() {
        System.out.println("[PELIS] Cargando lista de películas...");

        listaPeliculas.getItems().clear();
        hboxToSala.clear();

        for (Sala s : cine.getSalas()) {
            System.out.println("[PELIS] Procesando sala: " + s.getPelicula());

            ImageView img = new ImageView();
            try {
                Image mini = new Image(
                        getClass().getResourceAsStream("/cine/resources/" + s.getPortada())
                );
                img.setImage(mini);
            } catch (Exception e) {
                System.out.println("[PELIS] ERROR al cargar imagen: " + s.getPortada());
            }

            img.setFitWidth(50);
            img.setFitHeight(60);
            img.setPreserveRatio(true);
            img.setSmooth(true);

            Label lbl = new Label(s.getPelicula());
            lbl.setWrapText(true);

            HBox item = new HBox(10, img, lbl);
            item.setStyle("-fx-padding: 8; -fx-alignment: CENTER_LEFT;");

            hboxToSala.put(item, s);
            listaPeliculas.getItems().add(item);
        }

        System.out.println("[PELIS] Películas cargadas: " + listaPeliculas.getItems().size());
    }

    private void configurarSeleccionListaPeliculas() {

        System.out.println("[SEL-PELIS] Configurando selección de la lista de películas...");

        listaPeliculas.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, newItem) -> {

            if (newItem == null) {
                System.out.println("[SEL-PELIS] Nada seleccionado.");
                salaSeleccionada = null;
                imgPelicula.setImage(null);
                btnComprar.setDisable(true);
                return;
            }

            Sala s = hboxToSala.get(newItem);

            if (s == null) {
                System.out.println("[SEL-PELIS] No se encontró la sala asociada al item.");
                salaSeleccionada = null;
                imgPelicula.setImage(null);
                btnComprar.setDisable(true);
                return;
            }

            System.out.println("[SEL-PELIS] Sala seleccionada: " + s.getPelicula());

            salaSeleccionada = s;
            App.setSalaActual(s);

            try {
                Image grande = new Image(
                        getClass().getResourceAsStream("/cine/resources/" + s.getPortada())
                );
                imgPelicula.setImage(grande);
            } catch (Exception ex) {
                System.out.println("[SEL-PELIS] ERROR cargando imagen grande: " + s.getPortada());
                imgPelicula.setImage(null);
            }

            btnComprar.setDisable(false);
        });

        listaPeliculas.setOnMouseClicked(evt -> {
            if (evt.getClickCount() == 2) {
                System.out.println("[DBL-CLICK] Doble clic detectado en películas.");

                HBox sel = listaPeliculas.getSelectionModel().getSelectedItem();
                if (sel != null) {
                    Sala s = hboxToSala.get(sel);
                    if (s != null) {
                        System.out.println("[DBL-CLICK] Entrando a sala: " + s.getPelicula());
                        App.setSalaActual(s);
                        App.cambiarVentana("vista/fxml/ButacasView.fxml");
                    }
                }
            }
        });
    }

    @FXML
    private void comprar() {

        System.out.println("[COMPRAR] Botón comprar presionado.");

        if (salaSeleccionada == null) {
            System.out.println("[COMPRAR] No hay sala seleccionada. Acción ignorada.");
            return;
        }

        System.out.println("[COMPRAR] Sala seleccionada: " + salaSeleccionada.getPelicula());

        App.setSalaActual(salaSeleccionada);
        App.cambiarVentana("vista/fxml/ButacasView.fxml");
    }

    private Map<Compra, String> mapaCompras = new HashMap<>();

    private void cargarComprasAgrupadas() {

        System.out.println("[COMPRAS] Cargando compras agrupadas...");

        Cliente cliente = App.getClienteActual();
        if (cliente == null) {
            System.out.println("[COMPRAS] No hay cliente logueado.");
            listaCompras.getItems().clear();
            mapaCompras.clear();
            return;
        }

        listaCompras.getItems().clear();
        mapaCompras.clear();

        for (Compra compra : cine.getCompras()) {

            if (!compra.getCliente().equals(cliente)) continue;

            String titulo = compra.getSala().getPelicula() + " — " +
                    compra.getEntradas().size() + " butaca(s)";

            listaCompras.getItems().add(titulo);
            mapaCompras.put(compra, titulo);

            System.out.println("[COMPRAS] Compra agregada: " + titulo);
        }

        System.out.println("[COMPRAS] Total compras: " + listaCompras.getItems().size());
    }

    private void configurarDobleClickCompras() {

        System.out.println("[DBL-CLICK-COMPRAS] Configurando doble clic de compras...");

        listaCompras.setOnMouseClicked(evt -> {
            if (evt.getClickCount() == 2) {

                System.out.println("[DBL-CLICK-COMPRAS] Doble clic en compras.");

                String seleccion = listaCompras.getSelectionModel().getSelectedItem();
                if (seleccion == null) {
                    System.out.println("[DBL-CLICK-COMPRAS] Nada seleccionado.");
                    return;
                }

                Compra compraElegida = null;
                for (Map.Entry<Compra, String> entry : mapaCompras.entrySet()) {
                    if (entry.getValue().equals(seleccion)) {
                        compraElegida = entry.getKey();
                        break;
                    }
                }

                if (compraElegida == null) {
                    System.out.println("[DBL-CLICK-COMPRAS] Compra no encontrada.");
                    return;
                }

                System.out.println("[DBL-CLICK-COMPRAS] Mostrando ticket de compra ID: " + compraElegida.getId());
                App.setCompraActual(compraElegida);
                App.cambiarVentana("vista/fxml/TicketView.fxml");
            }
        });
    }

    @FXML
    private void cerrarSesion() {

        System.out.println("[SESION] Intentando cerrar sesión...");

        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setHeaderText("Cerrar sesión");
        a.setContentText("¿Seguro que deseas cerrar sesión?");

        if (a.showAndWait().get() == ButtonType.OK) {
            System.out.println("[SESION] Sesión cerrada. Volviendo al login.");
            App.setClienteActual(null);
            App.cambiarVentana("vista/fxml/LoginView.fxml");
        } else {
            System.out.println("[SESION] Cierre de sesión cancelado.");
        }
    }
}
