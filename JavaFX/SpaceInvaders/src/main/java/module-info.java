module poo2025_1.spaceinvaders {
    requires javafx.controls;
    requires javafx.fxml;

    opens poo2025_1.spaceinvaders to javafx.fxml;
    exports poo2025_1.spaceinvaders;
}
