package poo2025_1.spaceinvaders;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Polygon;

public class PrimaryController implements Initializable {

    @FXML
    private Polygon spaceShip;

    @FXML
    private AnchorPane rootPane; 

    @FXML
    private Label label;

    private double speed = 5.0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rootPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed((KeyEvent event) -> {
                    System.out.println("Tecla apertada");
                    if (event.getCode() == KeyCode.D) {
                        System.out.println("D apertado - movendo a nave para a direita");
                        spaceShip.setLayoutX(spaceShip.getLayoutX() + speed);
                    }
                    if (event.getCode() == KeyCode.A) {
                        System.out.println("A apertado - movendo a nave para a esquerda");
                        spaceShip.setLayoutX(spaceShip.getLayoutX() - speed);
                    }
                });
            }
        });
    }
}