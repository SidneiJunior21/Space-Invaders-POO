package poo2025_1.spaceinvaders.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import poo2025_1.spaceinvaders.core.GameController;

public class PauseMenuController {

    private GameController gameController;

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    @FXML
    private void continueGame() {
        if (gameController != null) {
            gameController.togglePause();
        }
    }

    @FXML
    private void restartGame() {
        if (gameController != null) {
            gameController.resetGame();
        }
    }

    @FXML
    private void quitGame() {
        Platform.exit();
    }
}