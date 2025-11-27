package app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ChangeSeatController {

    private Bus bus;

    @FXML private TextField txtCpf;
    @FXML private TextField txtNewSeat;
    @FXML private Label lblStatus;

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    @FXML
    private void handleChangeSeat(ActionEvent event) {
        if (bus == null) return;

        String cpf = txtCpf.getText().trim();
        int newSeat;

        try {
            newSeat = Integer.parseInt(txtNewSeat.getText().trim());
        } catch (NumberFormatException e) {
            lblStatus.setText("Invalid seat number. Please enter a number.");
            lblStatus.setStyle("-fx-text-fill: red;");
            return;
        }
        
        if (cpf.isEmpty()) {
            lblStatus.setText("Please enter the passenger's CPF.");
            lblStatus.setStyle("-fx-text-fill: red;");
            return;
        }

        // Call the Bus logic
        boolean success = bus.changeSeat(cpf, newSeat);

        if (success) {
            lblStatus.setText("✅ Seat changed successfully to " + newSeat + ".");
            lblStatus.setStyle("-fx-text-fill: green;");
            txtCpf.clear();
            txtNewSeat.clear();
        } else {
            // Failure status is set based on the result of the Bus method
            lblStatus.setText("Seat change failed. Check if seat is available.");
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