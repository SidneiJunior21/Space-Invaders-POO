package poo2025_1.spaceinvaders.core;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import poo2025_1.spaceinvaders.app.App;
import poo2025_1.spaceinvaders.entities.Bunker;
import poo2025_1.spaceinvaders.entities.Enemies;
import poo2025_1.spaceinvaders.entities.SpaceShip;

/**
 * Classe responsável por intermediar o que acontece 
 * na interface gráfica e o processamento necessário 
 * para o funcionamento do jogo.
 */
public class GameController implements Initializable {

    @FXML
    private Pane rootPane; 

    private Polygon spaceShipShape;

    private Polygon leftBunkerShape;

    private Polygon centerBunkerShape;
    
    private Polygon rightBunkerShape;

    private Bunker[] bunkers;

    private GameLoop gameLoop;

    private SpaceShip spaceShip;

    private Enemies enemies;

    private final double BASE_WIDTH = 600;
    
    private final double BASE_HEIGHT = 800;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        createShapes();

        setupBindings();

        rootPane.getChildren().addAll(
            spaceShipShape,
            leftBunkerShape,
            centerBunkerShape,
            rightBunkerShape
        );

        bunkers = new Bunker[] {
            new Bunker(leftBunkerShape), 
            new Bunker(centerBunkerShape), 
            new Bunker(rightBunkerShape)
        };

        spaceShip = new SpaceShip(spaceShipShape);

        enemies = new Enemies(rootPane);
        
        gameLoop = new GameLoop(spaceShip, enemies, bunkers);

        // espera a cena ser criada para adicionar os listeners
        rootPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {

                // Listener de Game Over
                newScene.addEventHandler(GameEvent.GAME_OVER, (GameEvent gameOver) -> {
                    System.out.println("Evento RESTART_GAME recebido pela CENA! Reiniciando...");
                    Platform.runLater(() -> resetGame());
                });

                // Listeners de teclado
                newScene.setOnKeyPressed((KeyEvent event) -> {
                    if (event.getCode() == KeyCode.D || event.getCode() == KeyCode.RIGHT) {
                        spaceShip.setMovingRight(true);
                    }
                    if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.LEFT) {
                        spaceShip.setMovingLeft(true);
                    }
                    if (event.getCode() == KeyCode.SPACE) {
                        spaceShip.setIsShooting(true);
                    }

                });
                
                newScene.setOnKeyReleased((KeyEvent event) -> {
                    if (event.getCode() == KeyCode.D || event.getCode() == KeyCode.RIGHT) {
                        spaceShip.setMovingRight(false);
                    }
                    if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.LEFT) {
                        spaceShip.setMovingLeft(false);
                    }
                    if (event.getCode() == KeyCode.SPACE) {
                        spaceShip.setIsShooting(false);
                    }
                });
            }
        });

        gameLoop.start();
    }

    // Para o loop do jogo atual e recarrega a cena principal
    private void resetGame()  {
        gameLoop.stop();

        try {

            App.setRoot("primary");

        }
        catch (IOException e) { }
    }

    /**
     * Cria os objetos Polygon para os elementos do jogo (nave e bunkers)
     * e define suas aparências iniciais (cores e traços).
     * Este método é chamado antes de configurar os bindings de posição e escala.
     */
    private void createShapes() {
        // --- Cria a nave do jogador (Player's Spaceship) ---
        spaceShipShape = new Polygon(
                -22.4, 15.6,
                22.4, 15.6,
                1.0, -18.6
        );
        spaceShipShape.setFill(Color.web("#841fff")); // Cor roxa
        spaceShipShape.setStroke(Color.TRANSPARENT); // Sem contorno

        // --- Cria os bunkers de defesa (Defense Bunkers) ---

        // Como todos os bunkers têm a mesma forma, podemos definir os pontos uma única vez
        // para reutilizar o código e mantê-lo limpo (princípio DRY).
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
        // --- VINCULANDO A POSIÇÃO ---

        // Posicionar a nave no centro horizontal e perto da base
        Platform.runLater(() -> {
            spaceShipShape.setLayoutX(rootPane.getWidth() / 2);
            spaceShipShape.setLayoutY(rootPane.getHeight() - 50);
        });

        // Posicionar os bunkers em porcentagens da largura
        // Bunker da esquerda a 20% da largura da tela
        leftBunkerShape.layoutXProperty().bind(rootPane.widthProperty().multiply(0.20));
        leftBunkerShape.layoutYProperty().bind(rootPane.heightProperty().subtract(150)); // 150 pixels da base

        centerBunkerShape.layoutXProperty().bind(rootPane.widthProperty().multiply(0.50)); // 50% da largura
        centerBunkerShape.layoutYProperty().bind(rootPane.heightProperty().subtract(150));

        rightBunkerShape.layoutXProperty().bind(rootPane.widthProperty().multiply(0.80)); // 80% da largura
        rightBunkerShape.layoutYProperty().bind(rootPane.heightProperty().subtract(150));

        // Você faria o mesmo para o bunker do centro (multiply(0.5)) e da direita (multiply(0.8))

        // --- VINCULANDO A ESCALA (TAMANHO) ---
        
        // Crie uma regra de escala que mantém a proporção.
        // Ela pega a menor das escalas (largura ou altura) para não distorcer.
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