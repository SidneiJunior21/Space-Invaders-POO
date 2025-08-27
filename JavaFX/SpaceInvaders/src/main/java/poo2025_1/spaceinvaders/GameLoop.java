package poo2025_1.spaceinvaders;

import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer{
    
    private final SpaceShip spaceShip;

    private final Enemies enemies;

    public GameLoop (SpaceShip spaceship, Enemies enemies){
        this.spaceShip = spaceship;
        this.enemies = enemies;
    }
    
    @Override
    public void handle(long now) {
        enemies.Move();
        spaceShip.Moves();
        spaceShip.ShootsWithDelay(now);
        spaceShip.MoveProjectiles();
    }
}