package pe.edu.upeu.sysventas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SysVentas extends Application {

    private static final double ANCHO = 1150;
    private static final double ALTO = 700;

    @Override
    public void start(Stage stage) throws Exception {
        // Ventana 1: Clientes
        FXMLLoader clienteLoader =
                new FXMLLoader(getClass().getResource("/view/cliente.fxml"));
        Scene clienteScene = new Scene(clienteLoader.load(), ANCHO, ALTO);
        agregarCss(clienteScene);

        Stage ventanaCliente = new Stage();
        ventanaCliente.setTitle("SysVentas - Clientes");
        ventanaCliente.setScene(clienteScene);
        ventanaCliente.setMinWidth(900);
        ventanaCliente.setMinHeight(600);

        // Ventana 2: Registro de licencias
        FXMLLoader registroLoader =
                new FXMLLoader(getClass().getResource("/view/main_licencia.fxml"));
        Scene registroScene = new Scene(registroLoader.load(), ANCHO, ALTO);
        agregarCss(registroScene);

        stage.setTitle("SysVentas - Registro de Licencias");
        stage.setScene(registroScene);
        stage.setMinWidth(950);
        stage.setMinHeight(600);

        // Las dos ventanas se muestran al iniciar.
        ventanaCliente.show();
        stage.show();

        // Si se cierra la ventana de registro, también se cierra Cliente
        stage.setOnCloseRequest(event -> ventanaCliente.close());

        // Si se cierra Cliente, también se cierra el registro
        ventanaCliente.setOnCloseRequest(event -> stage.close());
    }

    private void agregarCss(Scene scene) {
        var css = getClass().getResource("/css/style.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
