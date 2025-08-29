package poo2025_1.spaceinvaders;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Polygon;

/**
 * Classe responsável por intermediar o que acontece 
 * na interface gráfica e o processamento necessário 
 * para o funcionamento do jogo.
 */
public class GameController implements Initializable {

    @FXML
    private Polygon spaceShipShape;

    @FXML
    private AnchorPane rootPane; 

    @FXML
    private Polygon leftBunkerShape;

    @FXML
    private Polygon centerBunkerShape;
    
    @FXML
    private Polygon rightBunkerShape;

    private Bunker[] bunkers;

    private GameLoop gameLoop;

    private SpaceShip spaceShip;

    private Enemies enemies;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        bunkers = new Bunker[] {
            new Bunker(leftBunkerShape), 
            new Bunker(centerBunkerShape), 
            new Bunker(rightBunkerShape)
        };

        spaceShip = new SpaceShip(spaceShipShape);

        enemies = new Enemies(rootPane);
        
        gameLoop = new GameLoop(spaceShip, enemies, bunkers);

        // espera a cena ser criada para adicionar os listeners
        rootPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {

                // Listener de Game Over
                newScene.addEventHandler(GameEvent.GAME_OVER, (GameEvent gameOver) -> {
                    System.out.println("Evento RESTART_GAME recebido pela CENA! Reiniciando...");
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

            App.setRoot("primary");

        }
        catch (IOException e) { }
    }
}