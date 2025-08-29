package poo2025_1.spaceinvaders;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

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

    public boolean checkCollisionWith(Entity otherEntity) {

        boolean hasCollided = otherEntity.getShape().getBoundsInParent().intersects(shape.getBoundsInParent());

        return hasCollided;
    }

    public boolean checkCollisionWith(Shape otherObjectShape) {

        boolean hasCollided = otherObjectShape.getBoundsInParent().intersects(shape.getBoundsInParent());

        return hasCollided;
    }

    public int checkAllCollisionsWith(Iterable<?> otherObjects) {

        int numberOfCollisions = 0;

        for (Object otherObject : otherObjects) {

            boolean hasCollided = false;

            if (otherObject instanceof Shape){
                Shape otherObjectShape = (Shape) otherObject;
                hasCollided = checkCollisionWith(otherObjectShape);
            }

            if (otherObject instanceof Entity){
                Entity otherEntity = (Entity) otherObject;
                hasCollided = checkCollisionWith(otherEntity);
            }

            if (hasCollided)
                numberOfCollisions++;
        }

        return numberOfCollisions;
    }

    public void handleDeath () {
        setAlive(false);
        eraseShape();
    }

    public void  eraseShape () {

        // Checa se a entidade já foi removida
        if (shape.getParent() == null)
            return;
        
        if (shape.getParent() instanceof Pane) {

            Pane rootPane = (Pane) shape.getParent();

            rootPane.getChildren().remove(this.shape);

            return;

        }

        System.out.println("Parent da Entidade nao e instancia de Pane");
    }
}