package poo2025_1.spaceinvaders;

import javafx.scene.shape.Shape;

public class Enemy {

    private final Shape shape;

    private boolean isAlive;

    private final int row;

    private final int column;

    public Enemy(Shape shape, int row, int column) {
        this.shape = shape;
        this.isAlive = true;
        this.row = row;
        this.column = column;
    }

    public Shape getShape() { return shape; }

    public boolean isAlive() { return isAlive; }

    public void setAlive(boolean alive) { isAlive = alive; }

    public int getRow() { return row; }

    public int getColumn() { return column; }
    
}