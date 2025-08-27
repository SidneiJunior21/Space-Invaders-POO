package poo2025_1.spaceinvaders;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Polygon;

public class PrimaryController implements Initializable {

    @FXML
    private Polygon spaceShipShape;

    @FXML
    private AnchorPane rootPane; 

    private GameLoop gameLoop;

    private SpaceShip spaceShip;

    private Enemies enemies;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        spaceShip = new SpaceShip(spaceShipShape);

        enemies = new Enemies(rootPane);
        
        gameLoop = new GameLoop(spaceShip, enemies);

        // espera a cena ser criada para adicionar os listeners
        rootPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                //quando uma tecla é apertada
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
                // Quando a tecla é solta
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