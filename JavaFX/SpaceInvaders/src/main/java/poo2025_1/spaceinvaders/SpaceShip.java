package poo2025_1.spaceinvaders;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class SpaceShip {

    private final Polygon spaceShipShape;

    private boolean movingRight = false;

    private boolean movingLeft = false;

    private final double spaceShipSpeed = 5.0;

    private final List<Rectangle> projectiles;

    private final double projectileSpeed = 10.0;

    private boolean isShooting = false; 

    private long lastShotTime = 0;

    private final long cooldown = 80_000_000; // 80ms em nanos   

    public SpaceShip(Polygon spaceShipShape){
        this.spaceShipShape = spaceShipShape;
        this.projectiles = new ArrayList<>();
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

    public void Moves (){

        double currentX = spaceShipShape.getLayoutX();

        Bounds paneBounds = spaceShipShape.getParent().getLayoutBounds();

        Bounds shipBounds = spaceShipShape.getBoundsInParent();

        if (movingRight) {

            boolean isInBounds = (shipBounds.getMaxX() + spaceShipSpeed) <= paneBounds.getWidth();

            if (isInBounds)
                spaceShipShape.setLayoutX(currentX + spaceShipSpeed);
                
        }
        if (movingLeft) {

            boolean isInBounds = (shipBounds.getMinX() - spaceShipSpeed) >= 0;

            if (isInBounds)
                spaceShipShape.setLayoutX(currentX - spaceShipSpeed);

        }
    }

    public void ShootsWithDelay (long now) {

        boolean delayIsRespected = (now - lastShotTime) >= cooldown;

        if (isShooting && delayIsRespected){

            Rectangle projectile = CreateProjectile();

            projectiles.add(projectile);
            
            DrawProjectile(projectile);

            lastShotTime = now;
        }
    }

    /**
     * Move os projéteis contidos na lista de projéteis até que eles saiam da tela
     */
    public void MoveProjectiles(){
        // ao contrário para remover com segurança
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            Rectangle proj = projectiles.get(i);
            proj.setLayoutY(proj.getLayoutY() - projectileSpeed);

            // remove tiro quando sai da telaa
            if (proj.getLayoutY() < 0) {
                EraseProjectile(proj);
                projectiles.remove(i);
            }
        }
    }

    private Rectangle CreateProjectile(){
        Rectangle projectile = new Rectangle(5, 15); // largura, altura
        projectile.setStyle("-fx-fill: yellow;");
        
        // Pega os limites visuais da nave na tela
        Bounds shipBounds = spaceShipShape.getBoundsInParent();

        // posição X é o centro da nave
        // (borda esquerda + metade da largura) - (metade da largura do tiro)
        double startX = shipBounds.getMinX() + (shipBounds.getWidth() / 2) - (projectile.getWidth() / 2);
        // posição Y é o topo da nave
        double startY = shipBounds.getMinY();

        projectile.setLayoutX(startX);
        projectile.setLayoutY(startY);

        return projectile;
    }
    
    /**
     * Desenha um projétil na tela (obtida a partir da nave)
     * @param projectile
     *            O projétil a ser desenhado
     */
    private void DrawProjectile(Rectangle projectile){
        if (spaceShipShape.getParent() instanceof Pane){
            Pane parent = (Pane) spaceShipShape.getParent();
            parent.getChildren().add(projectile);
        }
    }

    /**
     * Apaga um projétil da tela
     * @param projectile
     *            O projétil a ser apagado
     */
    private void EraseProjectile(Rectangle projectile){
        if (spaceShipShape.getParent() instanceof Pane){
            Pane parent = (Pane) spaceShipShape.getParent();
            parent.getChildren().remove(projectile);
        }
    }

}