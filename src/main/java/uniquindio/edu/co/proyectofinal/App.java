package uniquindio.edu.co.proyectofinal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Carga la vista principal desde src/main/resources/uniquindio/edu/co/proyectofinal/
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("principal-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        stage.setTitle("CINEMAUQ - Sistema de Gestión");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}