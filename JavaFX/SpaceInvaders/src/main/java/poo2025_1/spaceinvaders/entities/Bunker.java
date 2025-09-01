package poo2025_1.spaceinvaders.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.geometry.Bounds;

import javafx.scene.shape.Shape;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

/**
 * Entidade que encapsula o Bunker. Contem pontos de vida e 
 * a logica por tras deles alem dos atributos e metodos de entidade.
 */
public class Bunker extends Entity {

    private int hitPoints;
    private final Pane rootPane;
    private final Random random = new Random();

    public Bunker (Shape shape, Pane rootPane){
        super(shape);
        this.hitPoints = 10;
        this.rootPane = rootPane;
    }

    /**
     * Diminui os pontos de vida de acordo com quantos tiros recebeu. 
     * Alem disso, caso o bunker chegue a 0 pontos de vida, ele morre.
     * @param projectiles A lista com todos os projeteis que podem atingir o bunker.
     */
    public void handlesGettingShot (List<Projectile> projectiles) {

        if(isAlive()){

            List<Projectile> projectilesToRemove = new ArrayList<>();

            for (Projectile projectile : projectiles) {

                boolean collisionHappened = checkCollisionWith(projectile);

                if (collisionHappened) {
                    hitPoints--;

                    createDamageMark(); 

                    projectile.handleDeath();

                    projectilesToRemove.add(projectile);
                }
            }

            projectiles.removeAll(projectilesToRemove);

            if (hitPoints <= 0)
                handleDeath();
                
        }
    }

    /**
     * Cria um pequeno círculo preto em uma posição aleatória sobre o bunker
     * para simular um dano de tiro.
     */
    private void createDamageMark() {
        Circle damageMark = new Circle(4, Color.BLACK);

        Bounds bunkerBounds = getShape().getBoundsInParent();

        double randomX = bunkerBounds.getMinX() + random.nextDouble() * bunkerBounds.getWidth();
        double randomY = bunkerBounds.getMinY() + random.nextDouble() * bunkerBounds.getHeight();

        damageMark.setLayoutX(randomX);
        damageMark.setLayoutY(randomY);

        this.rootPane.getChildren().add(damageMark);
    }
}