package poo2025_1.spaceinvaders;

import java.util.List;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Enemies {

    private List<Shape> enemyShapes;
    
    private final int ENEMY_ROWS = 5;
    private final int ENEMY_COLS = 8;

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