package poo2025_1.spaceinvaders;

import java.net.URL;
import java.util.ResourceBundle;
import java.io.IOException;

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

    private GameLoop gameLoop;

    private SpaceShip spaceShip;

    private Enemies enemies;

    // Para o loop do jogo atual e recarrega a cena principal
    public void resetGame() {
        gameLoop.stop();

        try {
            // Usa o método estático da classe App para recarregar o FXML
            App.setRoot("primary");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        spaceShip = new SpaceShip(spaceShipShape);

        enemies = new Enemies(rootPane);
        
        gameLoop = new GameLoop(spaceShip, enemies, this);

        // espera a cena ser criada para adicionar os listeners
        rootPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {

                newScene.setOnKeyPressed((KeyEvent event) -> {
                    if (event.getCode() == KeyCode.D) {
                        spaceShip.setMovingRight(true);
                    }
                    if (event.getCode() == KeyCode.A) {
                        spaceShip.setMovingLeft(true);
                    }
                    if (event.getCode() == KeyCode.SPACE) {
                        spaceShip.setIsShooting(true);
                    }

                });
                
                newScene.setOnKeyReleased((KeyEvent event) -> {
                    if (event.getCode() == KeyCode.D) {
                        spaceShip.setMovingRight(false);
                    }
                    if (event.getCode() == KeyCode.A) {
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
}