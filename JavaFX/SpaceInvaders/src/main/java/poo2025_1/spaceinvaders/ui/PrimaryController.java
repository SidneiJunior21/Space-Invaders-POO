package poo2025_1.spaceinvaders.ui;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.application.Platform;
import poo2025_1.spaceinvaders.app.App;

public class PrimaryController {

    @FXML
    private void switchToGame() throws IOException {
        App.setRoot("gamescene");
    }

    @FXML
    private void quitGame() {
        Platform.exit();
    }
}