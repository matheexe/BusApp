package src;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CheckInController {

    private Bus bus;
    
    // UI Elements (from FXML)
    @FXML private TextField txtCpf;
    @FXML private Label lblStatus;
    @FXML private ListView<String> listCheckedIn;

    // Observable list to connect to the ListView
    private ObservableList<String> checkedInList = FXCollections.observableArrayList();

    // Called by BusController to inject the Bus instance
    public void setBus(Bus bus) {
        this.bus = bus;
        listCheckedIn.setItems(checkedInList);
        refreshCheckedInList();
    }

    private void refreshCheckedInList() {
        if (bus != null) {
            // Clears and adds all passengers from the Stack
            checkedInList.clear();
            checkedInList.addAll(bus.getCheckedInPassengers());
        }
    }

    @FXML
    private void handleCheckIn(ActionEvent event) {
        if (bus == null) return;

        String cpf = txtCpf.getText().trim();
        if (cpf.isEmpty()) {
            lblStatus.setText("❌ Please enter the CPF.");
            lblStatus.setStyle("-fx-text-fill: red;");
            return;
        }

        // Calls the check-in logic in the Bus class
        boolean success = bus.checkInPassenger(cpf);

        if (success) {
            lblStatus.setText("Check-in successful! Welcome aboard.");
            lblStatus.setStyle("-fx-text-fill: green;");
            txtCpf.clear();
            refreshCheckedInList(); // Updates the Stack list
        } else {
            lblStatus.setText("⚠️ Check-in failed. Verify that the reservation is confirmed.");
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