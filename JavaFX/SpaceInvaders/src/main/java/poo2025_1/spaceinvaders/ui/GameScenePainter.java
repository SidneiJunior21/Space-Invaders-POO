package poo2025_1.spaceinvaders.ui;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Screen;

/**
 * Classe responsavel por inicializar as Entidades estáticas e a espaconave 
 * na cena inicial do jogo, com base no tamanho da tela.
 */
public class GameScenePainter {
    
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

    public GameScenePainter(Pane rootPane) {
        
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

    /**
     * Cria o Polygon da espaconave
     */
    private void createSpaceShipShape () {
        spaceShipShape = new Polygon(
                -22.4, 15.6,
                22.4, 15.6,
                1.0, -18.6
        );
        spaceShipShape.setFill(Color.web("#841fff")); // Cor roxa
        spaceShipShape.setStroke(Color.TRANSPARENT); // Sem contorno
    }

    /**
     * Cria os Polygons dos bunkers
     */
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

    /**
     * Vincula as proporcoes das entidades desenhadas ao tamanho da tela.
     */
    private void setupBindings() {
        ChangeListener<Number> sizeListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (rootPane.getWidth() > 0 && rootPane.getHeight() > 0) {
                    spaceShipShape.setLayoutX(rootPane.getWidth() / 2);
                    spaceShipShape.setLayoutY(rootPane.getHeight() - 50);

                    rootPane.widthProperty().removeListener(this);
                    rootPane.heightProperty().removeListener(this);
                }
            }
        };

        rootPane.widthProperty().addListener(sizeListener);
        rootPane.heightProperty().addListener(sizeListener);

        leftBunkerShape.layoutXProperty().bind(rootPane.widthProperty().multiply(0.20));
        leftBunkerShape.layoutYProperty().bind(rootPane.heightProperty().subtract(150));

        centerBunkerShape.layoutXProperty().bind(rootPane.widthProperty().multiply(0.50));
        centerBunkerShape.layoutYProperty().bind(rootPane.heightProperty().subtract(150));

        rightBunkerShape.layoutXProperty().bind(rootPane.widthProperty().multiply(0.80));
        rightBunkerShape.layoutYProperty().bind(rootPane.heightProperty().subtract(150));

        NumberBinding scaleBinding = Bindings.min(
                rootPane.widthProperty().divide(BASE_WIDTH),
                rootPane.heightProperty().divide(BASE_HEIGHT)
        );
   
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