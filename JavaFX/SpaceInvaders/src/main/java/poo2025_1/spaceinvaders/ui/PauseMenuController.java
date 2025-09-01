package poo2025_1.spaceinvaders.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import poo2025_1.spaceinvaders.core.GameController;

/**
 * Controller do menu de pausa.
 */
public class PauseMenuController {

    private GameController gameController;

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    @FXML
    public void continueGame() {
        if (gameController != null) {
            gameController.togglePause();
        }
    }

    @FXML
    public void restartGame() {
        if (gameController != null) {
            Platform.runLater(() -> gameController.restartGame());
        }
    }

    @FXML
    public void quitGame() {
        Platform.exit();
    }
}