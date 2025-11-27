package app;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CancelTicketController {

    private Bus bus;

    @FXML private TextField txtCpf;
    @FXML private Label lblStatus;

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    @FXML
    private void handleCancelTicket(ActionEvent event) {
        if (bus == null) return;

        String cpf = txtCpf.getText().trim();
        
        if (cpf.isEmpty()) {
            lblStatus.setText("❌ Please enter the passenger's CPF.");
            lblStatus.setStyle("-fx-text-fill: red;");
            return;
        }

        // Call the Bus logic
        boolean success = bus.cancelReservation(cpf);

        if (success) {
            lblStatus.setText("✅ Reservation successfully canceled.");
            lblStatus.setStyle("-fx-text-fill: green;");
            txtCpf.clear();
        } else {
            lblStatus.setText("⚠️ Cancellation failed. Active reservation not found.");
            lblStatus.setStyle("-fx-text-fill: orange;");
        }
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}