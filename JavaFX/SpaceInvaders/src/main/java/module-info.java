module poo2025_1.spaceinvaders {
    requires javafx.controls;
    requires javafx.fxml;

    // Necessário para controllers carregados pelo FXMLLoader
    opens poo2025_1.spaceinvaders.ui to javafx.fxml;
    opens poo2025_1.spaceinvaders.app to javafx.fxml;
    opens poo2025_1.spaceinvaders.core to javafx.fxml;


    // Exporta os pacotes principais
    exports poo2025_1.spaceinvaders.app;
    exports poo2025_1.spaceinvaders.core;
    exports poo2025_1.spaceinvaders.entities;
    exports poo2025_1.spaceinvaders.ui;
}

