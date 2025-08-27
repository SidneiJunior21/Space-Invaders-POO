package poo2025_1.spaceinvaders;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * Classe responsável por encapsular os inimigos. <p>
 * Contem todas as propriedades e metodos necessarios para o
 * funcionamento completo dos comportamentos dos inimigos, como
 * mover-se continuamente e morrer ao ser atingido por um projetil.
 */
public class Enemies {

    //variaveis novas do papoco
    private final List<Rectangle> enemyProjectiles;
    private final double projectileSpeed = 4.0;
    private long lastEnemyShotTime = 0;
    private final long enemyShotCooldown = 400_000_000;
    private final Random random = new Random();

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

        this.enemyProjectiles = new ArrayList<>();

        InitializeEnemies();
    }
    /**
     * Inicializa os inimigos na tela e na lista que os controlará
     */
    private void InitializeEnemies () {

        for (int row = 0; row < ENEMY_ROWS; row++) {

            for (int col = 0; col < ENEMY_COLS; col++) {

                Shape enemy = CreateEnemy(row, col);

                rootPane.getChildren().add(enemy);

                enemyShapes.add(enemy);
            }
        }
    }

    /**
     * Cria um inimigo, dada a linha e a coluna nas quais ele se localizará.
     * 
     * @param row A linha
     * @param col A coluna
     * <p>
     * @return O inimigo correspondente a essa posicao
     */
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

    /**
     * Método responsável por orquestrar o movimento contínuo dos inimigos:
     * Mover-se horizontalmente até que o inimigo mais próximo da borda colida com ela.
     * Após a colisão, inverte-se o sentido do movimento horizontal e incrementa a posicao
     * vertical de todos os inimigos.
     */
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

    // Verifica colisões entre projéteis e inimigos
    public void checkCollisions(List<Rectangle> projectiles) {
    for (int i = projectiles.size() - 1; i >= 0; i--) {
        Rectangle projectile = projectiles.get(i);
        for (int j = enemyShapes.size() - 1; j >= 0; j--) {
            Shape enemy = enemyShapes.get(j);
                if (enemy.isVisible() && projectile.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                    enemy.setVisible(false);
                    rootPane.getChildren().remove(projectile);
                    projectiles.remove(i);                    
                    break; 
                }
            }
        }
    }
    
    /**
     * Procura na lista de inimigos pelo inimigo vivo mais próximo da borda direita da tela.
     */
    private Shape GetLastEnemyToTheRight() {

        Shape enemyToTheRight = null;

        int lastMostToRightPositionInRow = ENEMY_COLS - 1;

         /** 
          * Percorre cada fileira da direita para esquerda, checando se o inimigo está vivo.
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

    /**
     * Procura na lista de inimigos pelo inimigo vivo mais próximo da borda esquerda da tela.
     */
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

    // Cria e posiciona um projétil vindo de um inimigo
    private void createProjectile(Shape enemy) {
        Rectangle projectile = new Rectangle(5, 15, Color.RED);
        Bounds enemyBounds = enemy.getBoundsInParent();
        double startX = enemyBounds.getMinX() + (enemyBounds.getWidth() / 2) - (projectile.getWidth() / 2);
        double startY = enemyBounds.getMaxY();

        projectile.setLayoutX(startX);
        projectile.setLayoutY(startY);

        enemyProjectiles.add(projectile);
        rootPane.getChildren().add(projectile);
    }

    // Move os projéteis dos inimigos e os remove se saírem da tela
    public void moveEnemyProjectiles() {
        Bounds paneBounds = rootPane.getLayoutBounds();
        for (int i = enemyProjectiles.size() - 1; i >= 0; i--) {
            Rectangle proj = enemyProjectiles.get(i);
            proj.setLayoutY(proj.getLayoutY() + projectileSpeed);

            if (proj.getLayoutY() > paneBounds.getHeight()) {
                rootPane.getChildren().remove(proj);
                enemyProjectiles.remove(i);
            }
        }
    }

    // Faz um inimigo aleatório atirar
    public void shoot(long now) {
        if ((now - lastEnemyShotTime) < enemyShotCooldown) {
            return;
        }
        List<Shape> visibleEnemies = new ArrayList<>();
        for (Shape enemy : enemyShapes) {
            if (enemy.isVisible()) {
                visibleEnemies.add(enemy);
            }
        }
        if (visibleEnemies.isEmpty()) {
            return;
        }
        Shape shooter = visibleEnemies.get(random.nextInt(visibleEnemies.size()));
        createProjectile(shooter);
        lastEnemyShotTime = now;
    }

    public List<Rectangle> getEnemyProjectiles() {
        return this.enemyProjectiles;
    }

    //Verifica se todos os inimigos na lista estão invisíveis
    public boolean areAllEnemiesDefeated() {
        for (Shape enemy : enemyShapes) {
            if (enemy.isVisible()) {
                // Encontrou pelo menos um inimigo vivo, então o jogo não acabou.
                return false;
            }
        }
        // Se o loop terminar, significa que nenhum inimigo visível foi encontrado.
        return true;
    }
}