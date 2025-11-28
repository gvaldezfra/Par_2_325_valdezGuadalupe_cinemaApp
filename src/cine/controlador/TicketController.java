package cine.controlador;

import cine.App;
import cine.modelo.Entrada;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class TicketController {

    @FXML private Label lblCliente;
    @FXML private Label lblPelicula;
    @FXML private Label lblSala;
    @FXML private ListView<String> listaButacas;

    private List<Entrada> entradas = new ArrayList<>();

    @FXML
    public void initialize() {

        // 1) Primero intentamos ver si viene un ticket múltiple (comprado recién)
        if (App.getEntradasActuales() != null && !App.getEntradasActuales().isEmpty()) {
            entradas = App.getEntradasActuales();
        }
        // 2) Sino, vemos si hay un ticket individual (compra pasada)
        else if (App.getEntradaActual() != null) {
            entradas.add(App.getEntradaActual());
        }
        // 3) Sino, no hay nada para mostrar
        else {
            return;
        }

        Entrada primera = entradas.get(0);

        lblCliente.setText("Cliente: " + primera.getCliente().getEmail());
        lblPelicula.setText("Película: " + primera.getSala().getPelicula());
        lblSala.setText("Sala: " + primera.getSala().getNumero());

        for (Entrada e : entradas) {
            String linea = "Fila " + e.getButaca().getFila() +
                            " - Asiento " + e.getButaca().getNumero();
            listaButacas.getItems().add(linea);
        }
    }

    @FXML
    private void exportarPDF() {
        try {
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream("ticket.pdf"));
            doc.open();

            Entrada primera = entradas.get(0);

            doc.add(new Paragraph("===== TICKET DE CINE =====\n\n"));
            doc.add(new Paragraph("Cliente: " + primera.getCliente().getEmail()));
            doc.add(new Paragraph("Película: " + primera.getSala().getPelicula()));
            doc.add(new Paragraph("Sala: " + primera.getSala().getNumero()));
            doc.add(new Paragraph("\nButacas:\n"));

            for (Entrada e : entradas) {
                doc.add(new Paragraph("- Fila " + e.getButaca().getFila() +
                                      " | Asiento " + e.getButaca().getNumero()));
            }

            doc.add(new Paragraph("\n¡Gracias por su compra!"));
            doc.close();

            System.out.println("PDF generado: ticket.pdf");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void volver() {
        App.cambiarVentana("vista/fxml/PeliculasView.fxml");
    }

    @FXML
    private void cerrar() {
        System.exit(0);
    }
}
