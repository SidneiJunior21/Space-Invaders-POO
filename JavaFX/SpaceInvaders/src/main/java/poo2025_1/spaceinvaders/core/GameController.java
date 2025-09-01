package poo2025_1.spaceinvaders.core;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import poo2025_1.spaceinvaders.app.App;
import poo2025_1.spaceinvaders.entities.Bunker;
import poo2025_1.spaceinvaders.entities.Enemies;
import poo2025_1.spaceinvaders.entities.SpaceShip;
import poo2025_1.spaceinvaders.ui.GameScenePainter;
import poo2025_1.spaceinvaders.ui.PauseMenuController;

/**
 * Classe responsável por intermediar o que acontece 
 * na interface gráfica e o processamento necessário 
 * para o funcionamento do jogo.
 */
public class GameController implements Initializable { 

    @FXML
    private StackPane rootStackPane;

    @FXML
    private Pane rootPane;

    private Bunker[] bunkers;

    private GameLoop gameLoop;

    private SpaceShip spaceShip;

    private Enemies enemies;

    private Parent pauseMenu;

    private boolean isPaused = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        GameScenePainter gameScenePainter = new GameScenePainter(rootPane);

        bunkers = new Bunker[] {
            new Bunker(gameScenePainter.getLeftBunkerShape()), 
            new Bunker(gameScenePainter.getCenterBunkerShape()), 
            new Bunker(gameScenePainter.getRightBunkerShape())
        };

        spaceShip = new SpaceShip(gameScenePainter.getSpaceShipShape());

        enemies = new Enemies(rootPane);
        
        gameLoop = new GameLoop(spaceShip, enemies, bunkers);

        loadPauseMenu();

        addInputListeners();

        gameLoop.start();
    }

    /**
     * Metodo responsavel por adicionar os listeners de input a cena do jogo.
     */
    private void addInputListeners() {
    
        // espera a cena ser criada para adicionar os listeners
        rootPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                // Listener de Game Over
                newScene.addEventHandler(GameEvent.GAME_OVER, (GameEvent gameOver) -> {
                    System.out.println("Evento GAME_OVER recebido pela CENA! Reiniciando...");
                    Platform.runLater(() -> restartGame());
                });

                newScene.setOnKeyPressed((KeyEvent event) -> {
                    if (event.getCode() == KeyCode.ESCAPE) {
                        togglePause();
                    }

                    // Só processa teclas de jogo se não estiver pausado
                    if (!isPaused) {
                        if (event.getCode() == KeyCode.D || event.getCode() == KeyCode.RIGHT) {
                            spaceShip.setMovingRight(true);
                        }
                        if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.LEFT) {
                            spaceShip.setMovingLeft(true);
                        }
                        if (event.getCode() == KeyCode.SPACE) {
                            spaceShip.setIsShooting(true);
                        }
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
    }

    /**
     * Carrega o FXML do menu de pausa.
     */
    private void loadPauseMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("pausemenu.fxml"));
            pauseMenu = loader.load();
            PauseMenuController pauseController = loader.getController();
            pauseController.setGameController(this); // Passa a instância atual para o controller do menu
        } catch (IOException e) { 
            System.out.println("Erro ao carregar o menu de pausa: " + e.getMessage());
        }
    }

    /**
     * Alterna entre os modos: pausado e despausado.
     */
    public void togglePause() {
        isPaused = !isPaused;
        if (isPaused) {
            gameLoop.stop();
            if (!rootStackPane.getChildren().contains(pauseMenu)) {
                rootStackPane.getChildren().add(pauseMenu); // Mostra o menu
            }
        } else {
            gameLoop.start();
            rootStackPane.getChildren().remove(pauseMenu); // Esconde o menu
        }
    }

    /**
     * Para a execucao atual e reinicia o jogo
     */
    public void restartGame()  {
        gameLoop.stop();
        try {
            App.setRoot("gamescene");
        } catch (IOException e) {
            System.out.println("Erro ao resetar o jogo: " + e.getMessage());
        }
    }
}
