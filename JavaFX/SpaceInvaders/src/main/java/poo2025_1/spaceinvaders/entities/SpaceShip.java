package poo2025_1.spaceinvaders.entities;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import poo2025_1.spaceinvaders.core.GameEvent;


/**
 * Classe responsavel por encapsular a espaconave (jogador). <p>
 * Contem todas as propriedades e metodos necessarios para o
 * funcionamento completo dos comportamentos da espaconave, como
 * mover-se e atirar.
 */
public class SpaceShip extends Entity {

    private boolean movingRight = false;

    private boolean movingLeft = false;

    private final double spaceShipSpeed = 5.0;

    private final List<Projectile> projectiles;

    private final double projectileSpeed = 10.0;

    private boolean isShooting = false; 

    private long lastShotTime = 0;

    private final long shotCooldown = 80_000_000; // 80ms em nanos   

    public SpaceShip(Polygon spaceShipShape){
        super(spaceShipShape);
        this.projectiles = new ArrayList<>();
    }

    public List<Projectile> getProjectiles () {
        return this.projectiles;
    }

    public void setMovingRight(boolean status){
        this.movingRight = status;
    }

    public void setMovingLeft(boolean status){
        this.movingLeft = status;
    }

    public void setIsShooting(boolean status){
        this.isShooting = status;
    }
    
    /**
     * Movimenta a espaconave caso seus booleanos de movimento sejam verdadeiros. <p>
     * Os booleanos são alterados no listener de KeyEvent do controller.
     * @see
     *          GameController.initialize 
     */
    public void moves (){

        double currentX = getShape().getLayoutX();

        Bounds paneBounds = getShape().getParent().getLayoutBounds();

        Bounds shipBounds = getShape().getBoundsInParent();

        if (movingRight) {

            boolean isInBounds = (shipBounds.getMaxX() + spaceShipSpeed) <= paneBounds.getWidth();

            if (isInBounds)
                getShape().setLayoutX(currentX + spaceShipSpeed);
                
        }
        if (movingLeft) {

            boolean isInBounds = (shipBounds.getMinX() - spaceShipSpeed) >= 0;

            if (isInBounds)
                getShape().setLayoutX(currentX - spaceShipSpeed);

        }
    }

    /**
     * Apaga um projétil da tela
     * @param now
     *          O timestamp do frame atual em nanosegundos, passado em gameLoop.handle( ). <p>
     *          Esse valor será o mesmo para todos os AnimationTimers chamados no mesmo frame. <p>
     * @see 
     *          GameLoop.handle
     */
    public void shootsWithDelay (long now) {

        boolean delayIsRespected = (now - lastShotTime) >= shotCooldown;

        if (isShooting && delayIsRespected){

            Projectile newProjectile = createProjectile();
            
            drawProjectile(newProjectile.getShape());

            lastShotTime = now;
        }
    }

    /**
     * Move os projéteis contidos na lista de projéteis até que eles saiam da tela
     */
    public void moveProjectiles (){
        // ao contrário para remover com segurança
        for (int i = projectiles.size() - 1; i >= 0; i--) {

            Projectile projectile = projectiles.get(i);

            projectile.getShape().setLayoutY(projectile.getShape().getLayoutY() - projectileSpeed);

            // remove tiro quando sai da telaa
            if (projectile.getShape().getLayoutY() < 0) {

                projectile.handleDeath();

                projectiles.remove(i);

            }
        }
    }

    /**
     * Cria, retorna e adiciona um projétil padrão à lista
     */
    private Projectile createProjectile (){
        Rectangle projectileShape = new Rectangle(5, 15); // largura, altura
        projectileShape.setStyle("-fx-fill: yellow;");
        
        // Pega os limites visuais da nave na tela
        Bounds shipBounds = getShape().getBoundsInParent();

        // posição X é o centro da nave
        // (borda esquerda + metade da largura) - (metade da largura do tiro)
        double startX = shipBounds.getMinX() + (shipBounds.getWidth() / 2) - (projectileShape.getWidth() / 2);
        // posição Y é o topo da nave
        double startY = shipBounds.getMinY();

        projectileShape.setLayoutX(startX);
        projectileShape.setLayoutY(startY);

        Projectile projectile = new Projectile(projectileShape);
        projectiles.add(projectile);

        return projectile;
    }
    
    /**
     * Desenha um projétil na tela (obtida a partir da nave)
     * @param projectileShape
     *            O projétil a ser desenhado
     */
    private void drawProjectile (Shape projectileShape){
        if (getShape().getParent() instanceof Pane rootPane){
            rootPane.getChildren().add(projectileShape);
        }
    }

    /**
     * Lida com o fato de ter morrido (colidido com um projetil).
     */
    public void handlesGettingShot (List<Projectile> enemyProjectiles) {
        if (isAlive()) {

            for (Projectile projectile : enemyProjectiles) {

                if (projectile.checkCollisionWith(this)) {
                    
                    setAlive(false);

                    projectile.handleDeath();
                    
                }
            }

            if (!isAlive())
                getShape().fireEvent(new GameEvent(GameEvent.GAME_OVER));
        }
    }
}