package src;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class BusController {

    @FXML
    private Label lblStatus;

    @FXML
    private TextField txtPassengerName;

    @FXML
    private void displayTripInfo() {
        lblStatus.setText("Botão clicado!");  // só para testar
        System.out.println("displayTripInfo() acionado");
    }
}