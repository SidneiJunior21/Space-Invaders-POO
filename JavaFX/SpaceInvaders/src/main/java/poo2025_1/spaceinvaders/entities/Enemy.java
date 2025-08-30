package poo2025_1.spaceinvaders.entities;

import javafx.scene.shape.Shape;

public class Enemy extends Entity{

    private final int row;

    private final int column;

    public Enemy(Shape shape, int row, int column) {
        super(shape);
        this.row = row;
        this.column = column;
    }

    public int getRow() { return row; }

    public int getColumn() { return column; }
    
}