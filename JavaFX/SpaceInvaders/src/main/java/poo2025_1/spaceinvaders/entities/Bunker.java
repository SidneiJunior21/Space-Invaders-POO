package poo2025_1.spaceinvaders.entities;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.shape.Shape;

/**
 * Entidade que encapsula o Bunker. Contem pontos de vida e 
 * a logica por tras deles alem dos atributos e metodos de entidade.
 */
public class Bunker extends Entity {

    private int hitPoints;

    public Bunker (Shape shape){
        super(shape);
        this.hitPoints = 10;
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

                    projectile.handleDeath();

                    projectilesToRemove.add(projectile);
                }
            }

            projectiles.removeAll(projectilesToRemove);

            if (hitPoints <= 0)
                handleDeath();
                
        }
    }
}