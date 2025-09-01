package poo2025_1.spaceinvaders.entities;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

/**
 * Classe abstrata que encapsula todas as Entidades do jogo.
 * Isto e: objetos que possam colidir uns com os outros e "morrer". <p>
 * Contem a logica necessaria para as colisoes e para processar a morte
 * da entidade.
 */
public abstract class Entity {
    
    private final Shape shape;

    private boolean isAlive;

    public Entity(Shape shape) {
        this.shape = shape;
        this.isAlive = true;
    }
    
    public Shape getShape() { return shape; }

    public boolean isAlive() { return isAlive; }

    public void setAlive(boolean status) { isAlive = status; }

    /**
     * Checa se houve colisao com outra entidade.
     * @param otherEntity A outra entidade.
     */
    public boolean checkCollisionWith(Entity otherEntity) {

        boolean hasCollided = otherEntity.getShape().getBoundsInParent().intersects(shape.getBoundsInParent());

        return hasCollided;
    }

    /**
     * Retorna o numero total de colisoes com uma colecao de entidades.
     * @param otherEntities O Iterable de Entidades a serem verificadas por colisoes.
     */
    public int checkAllCollisionsWith(Iterable<? extends Entity> otherEntities) {

        int numberOfCollisions = 0;

        for (Entity otherEntity : otherEntities) {

            boolean hasCollided = checkCollisionWith(otherEntity);

            if (hasCollided)
                numberOfCollisions++;
        }

        return numberOfCollisions;
    }

    /**
     * Lida com a morte! Torna a flag isAlive = false e
     * apaga o shape do Pane.
     */
    public void handleDeath () {
        setAlive(false);
        eraseShape();
    }

    /**
     * Apaga o Shape da Entidade do Pane onde ela esta.
     */
    public void  eraseShape () {

        // Checa se a entidade já foi removida
        if (shape.getParent() == null)
            return;
        
        if (shape.getParent() instanceof Pane rootPane) {


            rootPane.getChildren().remove(this.shape);

            return;

        }

        System.out.println("Parent da Entidade nao e instancia de Pane");
    }
}