package poo2025_1.spaceinvaders.entities;

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
import poo2025_1.spaceinvaders.core.GameEvent;

/**
 * Classe responsável por encapsular os inimigos. <p>
 * Contem todas as propriedades e metodos necessarios para o
 * funcionamento completo dos comportamentos dos inimigos, como
 * mover-se continuamente e morrer ao ser atingido por um projetil.
 */
public class Enemies {

    // Variaveis do papoco
    private final List<Projectile> projectiles;

    private final double projectileSpeed = 4.0;

    private long lastEnemyShotTime = 0;

    private final long enemyShotCooldown = 400_000_000;

    private final Random random = new Random();

    // Variaveis do desenho
    private final Pane rootPane;

    private final Enemy[][] enemyGrid;

    private final double shapeSize = 30;
    
    private final int ENEMY_ROWS = 5;

    private final int ENEMY_COLS = 8;

    // Variaveis da dinamica
    private final List<Enemy> livingEnemies;

    private int minLivingColumn = 0;

    private int maxLivingColumn = ENEMY_COLS - 1;

    private final double enemySpeed = 1.0;

    private int movementDirection = 1;

    public Enemies(Pane rootPane){

        this.rootPane = rootPane;

        this.enemyGrid = new Enemy[ENEMY_ROWS][ENEMY_COLS];

        this.livingEnemies = new ArrayList<>();

        this.projectiles = new ArrayList<>();

        initializeEnemies();
    }

    public List<Projectile> getProjectiles() { return this.projectiles; }

    /**
     * Inicializa os inimigos na tela e na lista que os controlará
     */
    private void initializeEnemies () {

        for (int row = 0; row < ENEMY_ROWS; row++) {

            for (int col = 0; col < ENEMY_COLS; col++) {

                Enemy enemy = createSingleEnemy(row, col);

                rootPane.getChildren().add(enemy.getShape());

                enemyGrid[row][col] = enemy;

                livingEnemies.add(enemy);
            }
        }
    }

    /**
     * Cria um inimigo, dada a linha e a coluna nas quais ele se localizará.
     * 
     * @param row A linha
     * @param column A coluna
     * <p>
     * @return O inimigo correspondente a essa posicao
     */
    private Enemy createSingleEnemy (int row, int column) {

        double x = (column * 60) + 50;

        double y = (row * 50) + 50;

        Shape enemyShape;

        switch (row) {
            case 0 -> {
                enemyShape = new Circle(shapeSize / 2, Color.DEEPPINK);
                x = (column * 60) + 65;
            }
            case 1, 2 -> enemyShape = new Rectangle(shapeSize, shapeSize, Color.AQUAMARINE);
            default -> {
                Polygon triangle = new Polygon();
                triangle.getPoints().addAll(shapeSize / 2, 0.0, 0.0, shapeSize, shapeSize, shapeSize);
                triangle.setFill(Color.ORANGE);
                enemyShape = triangle;
            }
        }

        enemyShape.setLayoutX(x);

        enemyShape.setLayoutY(y);

        return new Enemy(enemyShape, row, column);
    }

    /**
     * Método responsável por orquestrar o movimento contínuo dos inimigos:
     * Mover-se horizontalmente até que o inimigo mais próximo da borda colida com ela.
     * Após a colisão, inverte-se o sentido do movimento horizontal e incrementa a posicao
     * vertical de todos os inimigos.
     */
    public void move () {
        
        double enemyVelocity = enemySpeed * movementDirection;
        
        for (Enemy enemy : livingEnemies)
            enemy.getShape().setLayoutX(enemy.getShape().getLayoutX() + enemyVelocity);

        if (edgeReached()) {
            movementDirection *= -1;

            for (Enemy enemy : livingEnemies)
                enemy.getShape().setLayoutY(enemy.getShape().getLayoutY() + 5);
        }

    }

    /**
     * Checa se algum dos inimigos das colunas mais proximas das bordas colidiu com a borda correspondente.
     */
    private boolean edgeReached () {

        for (int row = 0; row < ENEMY_ROWS; row++) {

            Enemy rightmost = enemyGrid[row][maxLivingColumn];
            if (rightmost.isAlive() && rightmost.getShape().getBoundsInParent().getMaxX() >= rootPane.getWidth())
                return true;

            Enemy leftmost = enemyGrid[row][minLivingColumn];
            if (leftmost.isAlive() && leftmost.getShape().getBoundsInParent().getMinX() <= 0)
                return true;

        }
        
        return false;
    }

    /**
     * Lida com as colisoes entre projeteis da nave e inimigos.
     */
    public void handleGettingShot (List<Projectile> shipProjectiles) {

        List<Projectile> projectilesToRemove = new ArrayList<>();

        List<Enemy> killedEnemies = new ArrayList<>();

        for (Projectile projectile : shipProjectiles) {

            for (Enemy enemy : livingEnemies) {

                boolean collisionHappened = enemy.checkCollisionWith(projectile);
                
                if (collisionHappened) {

                    enemy.eraseShape();

                    projectile.handleDeath();

                    projectilesToRemove.add(projectile);

                    enemy.setAlive(false);

                    killedEnemies.add(enemy);

                    updateBorderColumns();

                    break; 
                }
            }
        }

        shipProjectiles.removeAll(projectilesToRemove);

        livingEnemies.removeAll(killedEnemies);

        if (livingEnemies.isEmpty()) {
            rootPane.fireEvent(new GameEvent(GameEvent.GAME_OVER));
        }
    }

    /**
     * Checa e atualiza se as bordas da formacao se manteem ou mudam conforme inimigos morrem
     */
    private void updateBorderColumns() {

        int aliveInMinColumn = 0;
        int aliveInMaxColumn = 0;

        for (int row = 0; row < ENEMY_ROWS; row++) {

            if (enemyGrid[row][minLivingColumn].isAlive())
                aliveInMinColumn++;

            if (enemyGrid[row][maxLivingColumn].isAlive())
                aliveInMaxColumn++;
                
        }

        if (aliveInMinColumn == 0 && minLivingColumn < ENEMY_COLS - 1)
            minLivingColumn++;

        if (aliveInMaxColumn == 0 && maxLivingColumn > 0)
            maxLivingColumn--;
    }

    /**
     * Cria e posiciona um projétil vindo de um inimigo
     */
    private void createProjectile(Shape enemyShape) {
        
        Rectangle projectileShape = new Rectangle(5, 15, Color.RED);

        Bounds enemyBounds = enemyShape.getBoundsInParent();

        double startX = enemyBounds.getMinX() + (enemyBounds.getWidth() / 2) - (projectileShape.getWidth() / 2);
        double startY = enemyBounds.getMaxY();

        projectileShape.setLayoutX(startX);
        projectileShape.setLayoutY(startY);

        projectiles.add(new Projectile(projectileShape));
        rootPane.getChildren().add(projectileShape);
    }

    /**
     * Move os projéteis dos inimigos e os remove se saírem da tela
     */
    public void moveProjectiles () {

        Bounds paneBounds = rootPane.getLayoutBounds();

        List<Projectile> projectilesToRemove = new ArrayList<>();
        
        for (Projectile projectile : projectiles) {

            Shape projectileShape = projectile.getShape();

            projectileShape.setLayoutY(projectileShape.getLayoutY() + projectileSpeed);

            if (projectileShape.getLayoutY() > paneBounds.getHeight()) {

                projectile.handleDeath();

                projectilesToRemove.add(projectile);
            }
        }
        projectiles.removeAll(projectilesToRemove);
    }

    /**
     * Faz um inimigo aleatório atirar
     */
    public void shoot (long now) {

        if ((now - lastEnemyShotTime) < enemyShotCooldown)
            return;

        if (livingEnemies.isEmpty())
            return;

        Enemy shooter = livingEnemies.get(random.nextInt(livingEnemies.size()));

        createProjectile(shooter.getShape());

        lastEnemyShotTime = now;

    }
}