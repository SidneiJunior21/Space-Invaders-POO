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

    // Variaveis do papoco
    private final List<Rectangle> projectiles;

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

    public List<Rectangle> getProjectiles() { return this.projectiles; }

    /**
     * Inicializa os inimigos na tela e na lista que os controlará
     */
    private void initializeEnemies () {

        for (int row = 0; row < ENEMY_ROWS; row++) {

            for (int col = 0; col < ENEMY_COLS; col++) {

                Enemy enemy = createEnemy(row, col);

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
    private Enemy createEnemy (int row, int column) {
        
        Shape enemyShape;
        switch (row) {
            case 0:
                enemyShape = new Circle(shapeSize / 2, Color.DEEPPINK);
                break;
            case 1:
            case 2:
                enemyShape = new Rectangle(shapeSize, shapeSize, Color.AQUAMARINE);
                break;
            default:
                Polygon triangle = new Polygon();
                triangle.getPoints().addAll(shapeSize / 2, 0.0, 0.0, shapeSize, shapeSize, shapeSize);
                triangle.setFill(Color.ORANGE);
                enemyShape = triangle;
                break;
        }

        double x = (column * 60) + 50;

        enemyShape.setLayoutX(x);

        double y = (row * 50) + 50;

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

        Bounds paneBounds = rootPane.getLayoutBounds();

        for (int row = 0; row < ENEMY_ROWS; row++) {

            if (enemyGrid[row][maxLivingColumn].getShape().getBoundsInParent().getMaxX() >= paneBounds.getWidth())
                return true;

            if (enemyGrid[row][minLivingColumn].getShape().getBoundsInParent().getMinX() <= 0)
                return true;
        }
        
        return false;
    }

    /**
     * Lida com as colisoes entre projeteis da nave e inimigos.
     */
    public void handleGettingShot (List<Rectangle> shipProjectiles) {

        List<Rectangle> projectilesToRemove = new ArrayList<>();

        List<Enemy> killedEnemies = new ArrayList<>();

        for (Rectangle projectile : shipProjectiles) {

            for (Enemy enemy : livingEnemies) {

                boolean collisionHappened = projectile.getBoundsInParent().intersects(enemy.getShape().getBoundsInParent());
                
                if (collisionHappened) {

                    rootPane.getChildren().remove(enemy.getShape());

                    rootPane.getChildren().remove(projectile);

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

            aliveInMinColumn += enemyGrid[row][minLivingColumn].isAlive() ? 1 : 0;

            aliveInMaxColumn += enemyGrid[row][maxLivingColumn].isAlive() ? 1 : 0;

        }

        if (aliveInMinColumn == 0)
            minLivingColumn++;

        if (aliveInMaxColumn == 0)
            maxLivingColumn--;
    }

    /**
     * Cria e posiciona um projétil vindo de um inimigo
     */
    private void createProjectile(Shape enemyShape) {
        
        Rectangle projectile = new Rectangle(5, 15, Color.RED);

        Bounds enemyBounds = enemyShape.getBoundsInParent();

        double startX = enemyBounds.getMinX() + (enemyBounds.getWidth() / 2) - (projectile.getWidth() / 2);
        double startY = enemyBounds.getMaxY();

        projectile.setLayoutX(startX);
        projectile.setLayoutY(startY);

        projectiles.add(projectile);
        rootPane.getChildren().add(projectile);
    }

    /**
     * Move os projéteis dos inimigos e os remove se saírem da tela
     */
    public void moveProjectiles () {

        Bounds paneBounds = rootPane.getLayoutBounds();

        for (int i = projectiles.size() - 1; i >= 0; i--) {

            Rectangle proj = projectiles.get(i);

            proj.setLayoutY(proj.getLayoutY() + projectileSpeed);

            if (proj.getLayoutY() > paneBounds.getHeight()) {

                rootPane.getChildren().remove(proj);

                projectiles.remove(i);
            }
        }
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