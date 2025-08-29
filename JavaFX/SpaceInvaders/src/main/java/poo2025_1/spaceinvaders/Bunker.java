package poo2025_1.spaceinvaders;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.shape.Shape;

public class Bunker extends Entity {

    private int hitPoints;

    public Bunker (Shape shape){
        super(shape);
        this.hitPoints = 10;
    }

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