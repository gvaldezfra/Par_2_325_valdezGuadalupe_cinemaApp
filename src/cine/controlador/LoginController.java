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

        System.out.println("[LOGIN] Intentando iniciar sesión...");

        try {
            String email = txtEmail.getText().trim();
            String pass = txtPass.getText().trim();

            System.out.println("[LOGIN] Email ingresado: " + email);

            if (email.isEmpty() || pass.isEmpty()) {
                System.out.println("[LOGIN] Faltan datos en el formulario.");
                alert("Datos incompletos", "Debes completar email y contraseña.");
                return;
            }

            for (Cliente c : cine.getClientes()) {

                System.out.println("[LOGIN] Verificando cliente: " + c.getEmail());

                if (c.getEmail().equalsIgnoreCase(email)) {

                    System.out.println("[LOGIN] Email encontrado. Verificando contraseña...");

                    if (!c.getPassword().equals(pass)) {
                        System.out.println("[LOGIN] Contraseña incorrecta.");
                        alert("Error de acceso", "Contraseña incorrecta.");
                        return;
                    }

                    System.out.println("[LOGIN] Inicio de sesión exitoso. Cliente: " + c.getNombre());

                    App.setClienteActual(c);
                    App.cambiarVentana("vista/fxml/PeliculasView.fxml");
                    return;
                }
            }

            System.out.println("[LOGIN] Usuario no encontrado: " + email);
            alert("Usuario no encontrado", "No existe un usuario con ese email.");

        } catch (Exception ex) {
            System.out.println("[ERROR-LOGIN] Error inesperado durante el login: " + ex.getMessage());
        }
    }

    @FXML
    private void irARegistro() {

        System.out.println("[REGISTRO] Cambiando a vista de registro...");

        try {
            App.cambiarVentana("vista/fxml/RegistroView.fxml");
            System.out.println("[REGISTRO] Vista cambiada correctamente.");
        } catch (Exception ex) {
            System.out.println("[ERROR-REGISTRO] Error al cambiar a registro: " + ex.getMessage());
        }
    }

    private void alert(String title, String msg) {
        System.out.println("[ALERTA] " + title + " - " + msg);
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
