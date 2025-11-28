package cine.controlador;

import cine.App;
import cine.modelo.Cine;
import cine.modelo.Cliente;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPass;

    private final Cine cine = App.cine;

    @FXML
    private void login() {

        String email = txtEmail.getText().trim();
        String pass = txtPass.getText().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            alert("Datos incompletos", "Debes completar email y contraseña.");
            return;
        }

        for (Cliente c : cine.getClientes()) {

            if (c.getEmail().equalsIgnoreCase(email)) {

                if (!c.getPassword().equals(pass)) {
                    alert("Error de acceso", "Contraseña incorrecta.");
                    return;
                }

                App.setClienteActual(c);
                App.cambiarVentana("vista/fxml/PeliculasView.fxml");
                return;
            }
        }

        alert("Usuario no encontrado", "No existe un usuario con ese email.");
    }

    @FXML
    private void irARegistro() {
        App.cambiarVentana("vista/fxml/RegistroView.fxml");
    }

    private void alert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
