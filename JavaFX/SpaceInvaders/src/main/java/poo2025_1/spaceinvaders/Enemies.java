package poo2025_1.spaceinvaders;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Enemies {

    private final Pane rootPane;

    private final List<Shape> enemyShapes;

    private final double shapeSize = 30;
    
    private final int ENEMY_ROWS = 5;

    private final int ENEMY_COLS = 8;

    private final double enemySpeed = 1.0;

    private int movementDirection = 1;

    public Enemies(Pane rootPane){

        this.rootPane = rootPane;

        enemyShapes = new ArrayList<>();

        InitializeEnemies();
    }

    private void InitializeEnemies () {

        for (int row = 0; row < ENEMY_ROWS; row++) {

            for (int col = 0; col < ENEMY_COLS; col++) {

                Shape enemy = CreateEnemy(row, col);

                rootPane.getChildren().add(enemy);

                enemyShapes.add(enemy);
            }
        }
    }

    private Shape CreateEnemy (int row, int col) {

        Shape enemy;

        switch (row) {
            case 0:
                enemy = new Circle(shapeSize / 2, Color.DEEPPINK);
                break;
            case 1:
            case 2:
                enemy = new Rectangle(shapeSize, shapeSize, Color.AQUAMARINE);
                break;
            default:
                Polygon triangle = new Polygon();
                triangle.getPoints().addAll(shapeSize / 2, 0.0, 0.0, shapeSize, shapeSize, shapeSize);
                triangle.setFill(Color.ORANGE);
                enemy = triangle;
                break;
        }

        double x = (col * 60) + 50;

        enemy.setLayoutX(x);

        double y = (row * 50) + 50;

        enemy.setLayoutY(y);

        return enemy;
    }

    public void Move () {
        
        Shape firstenemyToTheLeft = GetFirstEnemyToTheLeft();

        Shape lastEnemyToTheRight = GetLastEnemyToTheRight();

        Bounds paneBounds = rootPane.getLayoutBounds();        
        
        double enemyVelocity = enemySpeed * movementDirection;

        for (Shape enemyShape : enemyShapes)
            enemyShape.setLayoutX(enemyShape.getLayoutX() + enemyVelocity);

        boolean edgeReached = ( lastEnemyToTheRight.getBoundsInParent().getMaxX() ) >= paneBounds.getWidth()
                                || ( firstenemyToTheLeft.getBoundsInParent().getMinX() ) <= 0;

        if (edgeReached) {

            movementDirection *= -1;

            for (Shape enemyShape : enemyShapes)
                enemyShape.setLayoutY(enemyShape.getLayoutY() + 5);
        }


    }

    

    private Shape GetLastEnemyToTheRight() {

        Shape enemyToTheRight = null;

        int lastMostToRightPositionInRow = ENEMY_COLS - 1;

         /** 
          * Percorre cada fileira da direita para esqeurda, checando se o inimigo está vivo.
          * Caso esteja vivo e esteja mais a direita que o inimigo mais a direita da ultima
          * fileira, se torna o inimigo mais a direita atual.
          * Retorna o ultimo inimigo mais a direita.
          * !!! Considera que todos os inimigos estão alinhados verticalmente !!!
         */
        for (int row = 0; row < ENEMY_ROWS; row++) {

            int currentPositionInRow = ENEMY_COLS - 1;

            while (currentPositionInRow >= lastMostToRightPositionInRow){

                Shape currentEnemy = enemyShapes.get((row * ENEMY_COLS) + currentPositionInRow);

                boolean currentEnemyIsAlive = rootPane.getChildren().contains(currentEnemy);

                if (currentEnemyIsAlive){

                    enemyToTheRight = currentEnemy;

                    lastMostToRightPositionInRow = currentPositionInRow;
                    
                }

                currentPositionInRow--;
            }
        }

        return enemyToTheRight;
    }

    private Shape GetFirstEnemyToTheLeft() {

        Shape enemyToTheLeft = null;

        int lastMostToLeftPositionInRow = 0;

         /** 
          * Percorre cada fileira da esquerda para direita, checando se o inimigo está vivo.
          * Caso esteja vivo e esteja mais a esquerda que o inimigo mais a esquerda da ultima
          * fileira, se torna o inimigo mais a esquerda atual.
          * Retorna o ultimo inimigo mais a esquerda.
          * !!! Considera que todos os inimigos estão alinhados verticalmente !!!
         */
        for (int row = 0; row < ENEMY_ROWS; row++) {

            int currentPositionInRow = 0;

            while (currentPositionInRow <= lastMostToLeftPositionInRow){

                Shape currentEnemy = enemyShapes.get((row * ENEMY_COLS) + (currentPositionInRow));

                boolean currentEnemyIsAlive = rootPane.getChildren().contains(currentEnemy);

                if (currentEnemyIsAlive){

                    enemyToTheLeft = currentEnemy;

                    lastMostToLeftPositionInRow = currentPositionInRow;

                }

                currentPositionInRow++;
            }
        }

        return enemyToTheLeft;
    }
}