package poo2025_1.spaceinvaders.app;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {

        scene = new Scene(loadFXML("primary"), 640, 800);
        stage.setScene(scene);
        stage.setTitle("Space Invaders");
             
        stage.setResizable(false);
        stage.setFullScreen(true);

        stage.setFullScreenExitHint(""); // remove a mensagem
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); // desativa ESC

        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}