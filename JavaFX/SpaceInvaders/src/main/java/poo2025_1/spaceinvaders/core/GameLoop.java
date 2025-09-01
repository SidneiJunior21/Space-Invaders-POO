package poo2025_1.spaceinvaders.core;

import javafx.animation.AnimationTimer;
import poo2025_1.spaceinvaders.entities.Bunker;
import poo2025_1.spaceinvaders.entities.Enemies;
import poo2025_1.spaceinvaders.entities.SpaceShip;

/**
 * Classe responsável por gerar o loop de execucao do jogo.
 */
public class GameLoop extends AnimationTimer{
    
    private final SpaceShip spaceShip;

    private final Enemies enemies;

    private final Bunker[] bunkers;

    public GameLoop (SpaceShip spaceship, Enemies enemies, Bunker[] bunkers) {
        this.spaceShip = spaceship;
        this.enemies = enemies;
        this.bunkers = bunkers;
    }

    
    @Override
    public void handle(long now) {
        enemies.move();
        spaceShip.moves();
        spaceShip.shootsWithDelay(now);
        spaceShip.moveProjectiles();

        enemies.handleGettingShot(spaceShip.getProjectiles());
        enemies.randomEnemyShoot(now);
        enemies.moveProjectiles();
        spaceShip.handlesGettingShot(enemies.getProjectiles());

        for (Bunker bunker : bunkers) {
            bunker.handlesGettingShot(spaceShip.getProjectiles());
            bunker.handlesGettingShot(enemies.getProjectiles());
        }
    }
}