package poo2025_1.spaceinvaders;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class PrimaryController implements Initializable {

    @FXML
    private Polygon spaceShipShape;

    @FXML
    private AnchorPane rootPane; 

    private SpaceShip spaceShip;

    private GameLoop gameLoop;
    
    private List<Shape> enemies = new ArrayList<>();
    private double enemySpeed = 1.0;
    private int enemyDirection = 1;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //DrawEnemies();
        spaceShip = new SpaceShip(spaceShipShape);
        
        gameLoop = new GameLoop(spaceShip);

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

    boolean edgeReached = false;
    private void EnemiesMovement() {
        for (Shape enemy : enemies) {
            enemy.setLayoutX(enemy.getLayoutX() + (enemySpeed * enemyDirection));
            // if (enemy.getLayoutX() <= 0 || enemy.getLayoutX() >= rootPane.getWidth() - enemy.getBoundsInParent().getWidth()) {
            //     edgeReached = true;
            // }
        }/* 
        if (edgeReached) {
            enemyDirection *= -1;
            for (Shape enemy : enemies) {
            enemy.setLayoutY(enemy.getLayoutY() + 1);
            }
        }*/
    }
        

}