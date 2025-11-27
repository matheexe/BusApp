package src;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class ProcessReservationsController {

    private Bus bus;

    @FXML private ListView<String> listPending;
    @FXML private Button btnApprove;
    @FXML private Button btnReject;
    @FXML private Label lblStatus;

    public void setBus(Bus bus) {
        this.bus = bus;
        refreshPendingList();
    }

    private void refreshPendingList() {
        listPending.getItems().clear();
        if (bus != null) {
            // Note: This assumes bus.getPendingReservations() returns the Queue/List of pending Passengers
            for (Passenger p : bus.getPendingReservations()) {
                listPending.getItems().add(p.name + " - " + p.cpf);
            }
        }
    }

    @FXML
    private void handleApprove() {
        if (bus == null) return;

        // Calls the Bus method to process the next reservation (FIFO)
        Passenger next = bus.processNextReservation(); 

        if (next == null) {
            lblStatus.setText("No pending reservations or bus is full.");
            lblStatus.setStyle("-fx-text-fill: orange;");
        } else {
            lblStatus.setText("Approved: " + next.name + " (Seat " + next.seat + ")");
            lblStatus.setStyle("-fx-text-fill: green;");
        }

        refreshPendingList();
    }

    @FXML
    private void handleReject() {
        String selected = listPending.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblStatus.setText("Select a passenger first.");
            lblStatus.setStyle("-fx-text-fill: red;");
            return;
        }

        // Extracts the CPF from the list item string (e.g., "Name - 12345678900")
        String[] parts = selected.split(" - ");
        String cpf = parts.length > 1 ? parts[1] : "";

        // Calls the Bus method to reject the reservation
        boolean rejected = bus.rejectPendingReservation(cpf); 

        if (rejected) {
            lblStatus.setText("Reservation rejected for CPF: " + cpf);
            lblStatus.setStyle("-fx-text-fill: blue;");
        } else {
            lblStatus.setText("Error rejecting reservation.");
            lblStatus.setStyle("-fx-text-fill: red;");
        }
        
        refreshPendingList();
    }

    @FXML
    private void handleClose(ActionEvent event) {
        try {
            // 1. Gets the element that triggered the event (the button)
            Node source = (Node) event.getSource();
            
            // 2. Gets the Stage (Window) where the button is located
            Stage stage = (Stage) source.getScene().getWindow();
            
            // 3. Closes the window
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}