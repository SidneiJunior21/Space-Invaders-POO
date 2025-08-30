package poo2025_1.spaceinvaders.ui;

import java.io.IOException;

import javafx.fxml.FXML;
import poo2025_1.spaceinvaders.app.App;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}