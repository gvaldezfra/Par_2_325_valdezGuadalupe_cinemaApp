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
        String email = txtEmail.getText().trim().toLowerCase();
        String pass = txtPass.getText();

        if (nombre.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            alert(Alert.AlertType.WARNING, 
                  "Datos incompletos", 
                  "Todos los campos son obligatorios.");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            alert(Alert.AlertType.WARNING, 
                  "Email inválido", 
                  "El email ingresado no tiene un formato válido.");
            return;
        }

        if (pass.length() < 4) {
            alert(Alert.AlertType.WARNING, 
                  "Contraseña débil",
                  "La contraseña debe tener al menos 4 caracteres.");
            return;
        }

        for (Cliente c : cine.getClientes()) {
            if (c.getEmail().equalsIgnoreCase(email)) {
                alert(Alert.AlertType.WARNING, 
                      "Email duplicado", 
                      "Ya existe un usuario registrado con ese email.");
                return;
            }
        }

        Cliente nuevo = new Cliente(nombre, email, pass);
        cine.getClientes().add(nuevo);

        PersistenciaDatos.guardar(cine);

        alert(Alert.AlertType.INFORMATION, 
              "Registro exitoso", 
              "Usuario registrado correctamente.\nIniciando sesión...");

        App.setClienteActual(nuevo);
        App.cambiarVentana("vista/fxml/PeliculasView.fxml");
    }

    @FXML
    private void irALogin() {
        App.cambiarVentana("vista/fxml/LoginView.fxml");
    }

    private void alert(Alert.AlertType type, String title, String msg) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
