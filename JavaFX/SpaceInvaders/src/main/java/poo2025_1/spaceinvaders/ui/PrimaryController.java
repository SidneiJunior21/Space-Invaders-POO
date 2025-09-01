package poo2025_1.spaceinvaders.ui;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import poo2025_1.spaceinvaders.app.App;

/**
 * Controller da tela incial do jogo.
 */
public class PrimaryController {

    @FXML
    public void switchToGame() throws IOException {
        App.setRoot("gamescene");
    }

    @FXML
    public void quitGame() {
        Platform.exit();
    }
}