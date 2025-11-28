package cine.controlador;

import cine.App;
import cine.modelo.Cine;
import cine.modelo.Cliente;
import cine.persistencia.PersistenciaDatos;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RegistroController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPass;

    private final Cine cine = App.cine;

    @FXML
    private void registrar() {

        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();
        String pass = txtPass.getText().trim();

        if (nombre.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            alert("Datos incompletos", "Todos los campos son obligatorios.");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            alert("Email inválido", "El email ingresado no es válido.");
            return;
        }

        for (Cliente c : cine.getClientes()) {
            if (c.getEmail().equalsIgnoreCase(email)) {
                alert("Email duplicado", "Ya existe un usuario con ese email.");
                return;
            }
        }

        Cliente nuevo = new Cliente(nombre, email, pass);
        cine.getClientes().add(nuevo);

        // Guardar en cine.ser
        PersistenciaDatos.guardar(cine);

        alert("Registro exitoso", "Usuario registrado correctamente.\nIniciando sesión...");

        App.setClienteActual(nuevo);
        App.cambiarVentana("vista/fxml/PeliculasView.fxml");
    }

    @FXML
    private void irALogin() {
        App.cambiarVentana("vista/fxml/LoginView.fxml");
    }

    private void alert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
