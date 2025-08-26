package poo2025_1.spaceinvaders;

import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer{
    
    private final SpaceShip spaceShip;

    public GameLoop (SpaceShip spaceship){
        this.spaceShip = spaceship;
    }
    
    @Override
    public void handle(long now) {
        //EnemiesMovement();
        spaceShip.Moves();
        spaceShip.ShootsWithDelay(now);
        spaceShip.MoveProjectiles();
    }
}