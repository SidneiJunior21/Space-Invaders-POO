package poo2025_1.spaceinvaders.core;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import poo2025_1.spaceinvaders.app.App;
import poo2025_1.spaceinvaders.entities.Bunker;
import poo2025_1.spaceinvaders.entities.Enemies;
import poo2025_1.spaceinvaders.entities.SpaceShip;
import poo2025_1.spaceinvaders.ui.GuiManager;

/**
 * Classe responsável por intermediar o que acontece 
 * na interface gráfica e o processamento necessário 
 * para o funcionamento do jogo.
 */
public class GameController implements Initializable { 

    @FXML
    private Pane rootPane;

    private Bunker[] bunkers;

    private GameLoop gameLoop;

    private SpaceShip spaceShip;

    private Enemies enemies;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        GuiManager guiManager = new GuiManager(rootPane);

        bunkers = new Bunker[] {
            new Bunker(guiManager.getLeftBunkerShape()), 
            new Bunker(guiManager.getCenterBunkerShape()), 
            new Bunker(guiManager.getRightBunkerShape())
        };

        spaceShip = new SpaceShip(guiManager.getSpaceShipShape());

        enemies = new Enemies(rootPane);
        
        gameLoop = new GameLoop(spaceShip, enemies, bunkers);

        // espera a cena ser criada para adicionar os listeners
        rootPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {

                // Listener de Game Over
                newScene.addEventHandler(GameEvent.GAME_OVER, (GameEvent gameOver) -> {
                    System.out.println("Evento GAME_OVER recebido pela CENA! Reiniciando...");
                    Platform.runLater(() -> resetGame());
                });

                // Listeners de teclado
                newScene.setOnKeyPressed((KeyEvent event) -> {
                    if (event.getCode() == KeyCode.D || event.getCode() == KeyCode.RIGHT) {
                        spaceShip.setMovingRight(true);
                    }
                    if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.LEFT) {
                        spaceShip.setMovingLeft(true);
                    }
                    if (event.getCode() == KeyCode.SPACE) {
                        spaceShip.setIsShooting(true);
                    }

                });
                
                newScene.setOnKeyReleased((KeyEvent event) -> {
                    if (event.getCode() == KeyCode.D || event.getCode() == KeyCode.RIGHT) {
                        spaceShip.setMovingRight(false);
                    }
                    if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.LEFT) {
                        spaceShip.setMovingLeft(false);
                    }
                    if (event.getCode() == KeyCode.SPACE) {
                        spaceShip.setIsShooting(false);
                    }
                });
            }
        });

        gameLoop.start();
    }

    // Para o loop do jogo atual e recarrega a cena principal
    private void resetGame()  {
        gameLoop.stop();

        try {

            App.setRoot("gamescene");

        }
        catch (IOException e) { }
    }
}