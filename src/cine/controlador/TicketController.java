package cine.controlador;

import cine.App;
import cine.modelo.Compra;
import cine.modelo.Entrada;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.FileOutputStream;
import javafx.stage.FileChooser;

public class TicketController {

    @FXML
    private Label lblCliente;
    @FXML
    private Label lblFecha;
    @FXML
    private Label lblPelicula;
    @FXML
    private Label lblSala;
    @FXML
    private ListView<String> listaButacas;

    private Compra compra;

    @FXML
    public void initialize() {

        compra = App.getCompraActual();
        App.setCompraActual(null);

        if (compra == null) {
            return;
        }

        lblCliente.setText("Cliente: " + compra.getCliente().getEmail());
        lblFecha.setText("Fecha: " + compra.getFecha());
        lblPelicula.setText("Película: " + compra.getSala().getPelicula());
        lblSala.setText("Sala: " + compra.getSala().getNumero());

        for (Entrada e : compra.getEntradas()) {

            int fila = e.getButaca().getFila() + 1;
            char asiento = (char) ('A' + e.getButaca().getNumero());

            listaButacas.getItems().add("Fila " + fila + " — Asiento " + asiento);
        }
    }

    @FXML
    private void exportarPDF() {

        if (compra == null) {
            return;
        }

        try {

            // === Seleccionar destino con FileChooser ===
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar Ticket");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo PDF", "*.pdf"));
            fileChooser.setInitialFileName("ticket-" + compra.getSala().getPelicula() + ".pdf");

            File destino = fileChooser.showSaveDialog(null);
            if (destino == null) {
                return; 
            }
            // Creo el pdf
            Document doc = new Document(PageSize.A6);
            PdfWriter.getInstance(doc, new FileOutputStream(destino));
            doc.open();

            // Fuente, estilos
            Font tituloFont = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font normal = new Font(Font.HELVETICA, 11);
            Font bold = new Font(Font.HELVETICA, 11, Font.BOLD);

            Paragraph titulo = new Paragraph("TICKET DE COMPRA\n\n", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            doc.add(titulo);

            // Datos del cliente
            PdfPTable tablaInfo = new PdfPTable(1);
            tablaInfo.setWidthPercentage(100);

            tablaInfo.addCell(celda("Cliente: " + compra.getCliente().getEmail(), normal));
            tablaInfo.addCell(celda("Fecha: " + compra.getFecha(), normal));
            tablaInfo.addCell(celda("Película: " + compra.getSala().getPelicula(), normal));
            tablaInfo.addCell(celda("Sala: " + compra.getSala().getNumero(), normal));

            doc.add(tablaInfo);

            doc.add(new Paragraph("\n------------------------------------------------------------\n", normal));

            Paragraph butacasTitulo = new Paragraph("BUTACAS\n\n", bold);
            butacasTitulo.setAlignment(Element.ALIGN_CENTER);
            doc.add(butacasTitulo);

            // === LISTA DE BUTACAS ===
            for (Entrada e : compra.getEntradas()) {
                int fila = e.getButaca().getFila() + 1;
                char asiento = (char) ('A' + e.getButaca().getNumero());
                doc.add(new Paragraph("• Fila " + fila + " — Asiento " + asiento, normal));
            }

            doc.add(new Paragraph("\n------------------------------------------------------------\n", normal));

            Paragraph gracias = new Paragraph("¡Gracias por su compra!", bold);
            gracias.setAlignment(Element.ALIGN_CENTER);
            doc.add(gracias);

            doc.close();

            System.out.println("PDF generado correctamente");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PdfPCell celda(String texto, Font fuente) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, fuente));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5);
        return cell;
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
