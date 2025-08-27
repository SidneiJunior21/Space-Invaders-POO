package poo2025_1.spaceinvaders;

import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer{
    
    private final SpaceShip spaceShip;

    private final Enemies enemies;

    private final GameController gameController;

    public GameLoop (SpaceShip spaceship, Enemies enemies, GameController controller){
        this.spaceShip = spaceship;
        this.enemies = enemies;
        this.gameController = controller;
    }

    
    @Override
    public void handle(long now) {
        enemies.Move();
        spaceShip.Moves();
        spaceShip.ShootsWithDelay(now);
        spaceShip.MoveProjectiles();

        enemies.checkCollisions(spaceShip.getProjectiles());
        enemies.shoot(now);
        enemies.moveEnemyProjectiles();
        if (enemies.areAllEnemiesDefeated()) {
            gameController.resetGame();
            return;
        }
        if (spaceShip.isHitBy(enemies.getEnemyProjectiles())) {
            gameController.resetGame();
            return;
        }
    }
}