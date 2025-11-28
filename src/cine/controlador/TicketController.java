package cine.controlador;

import cine.App;
import cine.modelo.Compra;
import cine.modelo.Entrada;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.FileOutputStream;

public class TicketController {

    @FXML private Label lblCliente;
    @FXML private Label lblPelicula;
    @FXML private Label lblSala;
    @FXML private ListView<String> listaButacas;

    private Compra compra;

    @FXML
    public void initialize() {

        // === OBTENER COMPRA ACTUAL ===
        compra = App.getCompraActual();
        App.setCompraActual(null); // limpiar después de usar

        if (compra == null) return;

        lblCliente.setText("Cliente: " + compra.getCliente().getEmail());
        lblPelicula.setText("Película: " + compra.getSala().getPelicula());
        lblSala.setText("Sala: " + compra.getSala().getNumero());

        // === CARGAR BUTACAS ===
        for (Entrada e : compra.getEntradas()) {

            int fila = e.getButaca().getFila() + 1; // empieza en 1
            char asiento = (char) ('A' + e.getButaca().getNumero());

            listaButacas.getItems().add("Fila " + fila + " — Asiento " + asiento);
        }
    }

    @FXML
    private void exportarPDF() {
        if (compra == null) return;

        try {
            Document doc = new Document(PageSize.A6);
            PdfWriter.getInstance(doc, new FileOutputStream("ticket.pdf"));
            doc.open();

            doc.add(new Paragraph("====== TICKET DE CINE ======\n\n"));
            doc.add(new Paragraph("Cliente: " + compra.getCliente().getEmail()));
            doc.add(new Paragraph("Película: " + compra.getSala().getPelicula()));
            doc.add(new Paragraph("Sala: " + compra.getSala().getNumero()));
            doc.add(new Paragraph("\nButacas:\n"));

            for (Entrada e : compra.getEntradas()) {
                int fila = e.getButaca().getFila() + 1;
                char asiento = (char) ('A' + e.getButaca().getNumero());
                doc.add(new Paragraph("• Fila " + fila + " — Asiento " + asiento));
            }

            doc.add(new Paragraph("\nGracias por su compra!"));
            doc.close();

            System.out.println("PDF generado correctamente");

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
