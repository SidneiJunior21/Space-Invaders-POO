package poo2025_1.spaceinvaders;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.List; 

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.animation.AnimationTimer;
import javafx.geometry.Bounds;

//import de formas
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

public class PrimaryController implements Initializable {

    @FXML
    private Polygon spaceShip;

    @FXML
    private AnchorPane rootPane; 

    @FXML
    private Label label;

    private double speed = 5.0;

    private boolean moveLeft = false;
    private boolean moveRight = false;
    
    private List<Rectangle> projectiles = new ArrayList<>();
    private double projectileSpeed = 10.0;
    
    private List<Shape> enemies = new ArrayList<>();
    private double enemySpeed = 1.0;
    private final int ENEMY_ROWS = 5;
    private final int ENEMY_COLS = 8;
    private int enemyDirection = 1;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // espera a cena ser criada para adicionar os listeners
        rootPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                //quando uma tecla é apertada
                newScene.setOnKeyPressed((KeyEvent event) -> {
                    if (event.getCode() == KeyCode.D) {
                        moveRight = true;
                    }
                    if (event.getCode() == KeyCode.A) {
                        moveLeft = true;
                    }
                    if (event.getCode() == KeyCode.SPACE) {
                        shoot();
                    }

                });
                // Quando a tecla é solta
                newScene.setOnKeyReleased((KeyEvent event) -> {
                    if (event.getCode() == KeyCode.D) {
                        moveRight = false;
                    }
                    if (event.getCode() == KeyCode.A) {
                        moveLeft = false;
                    }
                });
            }
        });

        // loop para movimento contínuo
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                EnemiesMovement();
                // Pega a posição atual da nave
                double currentX = spaceShip.getLayoutX();

                if (moveRight) {
                    // faz com q a nave n saia pela direita
                    if (currentX + speed < rootPane.getWidth() - spaceShip.getBoundsInParent().getWidth()) {
                        spaceShip.setLayoutX(currentX + speed);
                    }
                }
                if (moveLeft) {
                    // faz com q a nave n saia pela esquerda
                    if (currentX - speed > 0) {
                        spaceShip.setLayoutX(currentX - speed);
                    }
                }
                 // movimento dos projéteis
                // ao contrário para remover com segurança
                for (int i = projectiles.size() - 1; i >= 0; i--) {
                    Rectangle proj = projectiles.get(i);
                    proj.setLayoutY(proj.getLayoutY() - projectileSpeed);

                    // remove tiro quando sai da telaa
                    if (proj.getLayoutY() < 0) {
                        rootPane.getChildren().remove(proj);
                        projectiles.remove(i);
                    }
                }
            }
        };
        gameLoop.start();
        DrawEnemies();
    }
    private void shoot() {
        Rectangle projectile = new Rectangle(5, 15); // largura, altura
        projectile.setStyle("-fx-fill: yellow;");
        
        // Pega os limites visuais da nave na tela
        Bounds shipBounds = spaceShip.getBoundsInParent();

        // posição X é o centro da nave
        // (borda esquerda + metade da largura) - (metade da largura do tiro)
        double startX = shipBounds.getMinX() + (shipBounds.getWidth() / 2) - (projectile.getWidth() / 2);
        // posição Y é o topo da nave
        double startY = shipBounds.getMinY();

        projectile.setLayoutX(startX);
        projectile.setLayoutY(startY);

        // adiciona o tiro a cena e a lista
        rootPane.getChildren().add(projectile);
        projectiles.add(projectile);
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
        
    private void DrawEnemies() {
        double shapeSize = 30;
        for (int row = 0; row < ENEMY_ROWS; row++) {
            for (int col = 0; col < ENEMY_COLS; col++) {
                Shape enemy;
                if (row == 0) {
                    enemy = new Circle(shapeSize / 2, Color.DEEPPINK);
                } else if (row == 1 || row == 2) {
                    enemy = new Rectangle(shapeSize, shapeSize, Color.AQUAMARINE);
                } else {
                    Polygon triangle = new Polygon();
                    triangle.getPoints().addAll(shapeSize / 2, 0.0, 0.0, shapeSize, shapeSize, shapeSize);
                    triangle.setFill(Color.ORANGE);
                    enemy = triangle;
                }
                double x = col * 60 + 50;
                double y = row * 50 + 50;
                enemy.setLayoutX(x);
                enemy.setLayoutY(y);
                rootPane.getChildren().add(enemy);
                enemies.add(enemy);
            }
        }
    }
}