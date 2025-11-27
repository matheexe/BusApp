module com.seuprojeto.avaliacao {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop; 

    opens app to javafx.fxml, javafx.graphics;
}