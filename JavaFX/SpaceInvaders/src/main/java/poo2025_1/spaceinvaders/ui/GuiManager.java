package poo2025_1.spaceinvaders.ui;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Screen;

/**
 *
 */
public class GuiManager {
    
    private final Pane rootPane;

    private Polygon spaceShipShape;

    private Polygon leftBunkerShape;

    private Polygon centerBunkerShape;
    
    private Polygon rightBunkerShape;

    private final double BASE_WIDTH = Screen.getPrimary().getVisualBounds().getWidth();
    
    private final double BASE_HEIGHT = Screen.getPrimary().getVisualBounds().getHeight();

    public Polygon getSpaceShipShape() { return this.spaceShipShape; }

    public Polygon getLeftBunkerShape() { return this.leftBunkerShape; }

    public Polygon getCenterBunkerShape() { return this.centerBunkerShape; }

    public Polygon getRightBunkerShape() { return this.rightBunkerShape; }

    public GuiManager(Pane rootPane) {
        
        this.rootPane = rootPane;

        createShapes();

        setupBindings();

        rootPane.getChildren().addAll(
            spaceShipShape,
            leftBunkerShape,
            centerBunkerShape,
            rightBunkerShape
        );

    }

    /**
     * Cria os objetos Polygon para os elementos do jogo (nave e bunkers)
     * e define suas aparências iniciais (cores e traços).
     * Este método é chamado antes de configurar os bindings de posição e escala.
     */
    private void createShapes() {

        createSpaceShipShape();

        createBunkersShapes();
    }

    private void createSpaceShipShape () {
        spaceShipShape = new Polygon(
                -22.4, 15.6,
                22.4, 15.6,
                1.0, -18.6
        );
        spaceShipShape.setFill(Color.web("#841fff")); // Cor roxa
        spaceShipShape.setStroke(Color.TRANSPARENT); // Sem contorno
    }

    private void createBunkersShapes() {
        double[] bunkerPoints = new double[]{
            -50.0, -45.6,
            25.0, -45.6,
            25.0, -14.6,
            9.0, -14.6,
            9.0, -32.2,
            -33.0, -32.2,
            -33.0, -14.6,
            -50.0, -14.6
        };

        // Bunker da Esquerda
        leftBunkerShape = new Polygon(bunkerPoints);
        leftBunkerShape.setFill(Color.DODGERBLUE);
        leftBunkerShape.setStroke(Color.BLACK);

        // Bunker do Centro
        centerBunkerShape = new Polygon(bunkerPoints);
        centerBunkerShape.setFill(Color.DODGERBLUE);
        centerBunkerShape.setStroke(Color.BLACK);

        // Bunker da Direita
        rightBunkerShape = new Polygon(bunkerPoints);
        rightBunkerShape.setFill(Color.DODGERBLUE);
        rightBunkerShape.setStroke(Color.BLACK);
    }

    private void setupBindings() {
        
        // Inicializando a posição do jogador no meio da tela
        Platform.runLater(() -> {
            spaceShipShape.setLayoutX(rootPane.getWidth() / 2);
            spaceShipShape.setLayoutY(rootPane.getHeight() - 50);
        });

        // Posicionar os bunkers em porcentagens da largura
        leftBunkerShape.layoutXProperty().bind(rootPane.widthProperty().multiply(0.20)); // 20% da largura
        leftBunkerShape.layoutYProperty().bind(rootPane.heightProperty().subtract(150));

        centerBunkerShape.layoutXProperty().bind(rootPane.widthProperty().multiply(0.50)); // 50% da largura
        centerBunkerShape.layoutYProperty().bind(rootPane.heightProperty().subtract(150));

        rightBunkerShape.layoutXProperty().bind(rootPane.widthProperty().multiply(0.80)); // 80% da largura
        rightBunkerShape.layoutYProperty().bind(rootPane.heightProperty().subtract(150));

        NumberBinding scaleBinding = Bindings.min(
                rootPane.widthProperty().divide(BASE_WIDTH),
                rootPane.heightProperty().divide(BASE_HEIGHT)
        );

        // Aplique a mesma regra de escala para todos os objetos
        spaceShipShape.scaleXProperty().bind(scaleBinding);
        spaceShipShape.scaleYProperty().bind(scaleBinding);
        
        leftBunkerShape.scaleXProperty().bind(scaleBinding);
        leftBunkerShape.scaleYProperty().bind(scaleBinding);
        
        centerBunkerShape.scaleXProperty().bind(scaleBinding);
        centerBunkerShape.scaleYProperty().bind(scaleBinding);

        rightBunkerShape.scaleXProperty().bind(scaleBinding);
        rightBunkerShape.scaleYProperty().bind(scaleBinding);
    }
}
