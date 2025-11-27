package src;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.util.List;

public class SoldOutTripsController {

    private TripScheduler scheduler;

    @FXML private ListView<String> listSoldOutTrips;
    @FXML private Label lblStatus;

    public void setScheduler(TripScheduler scheduler) {
        this.scheduler = scheduler;
        refreshSoldOutTrips();
    }

    private void refreshSoldOutTrips() {
        if (scheduler == null) return;
        
        listSoldOutTrips.getItems().clear();
        
        // Chama a lógica do scheduler
        List<String> soldOutList = scheduler.getSoldOutTrips();
        
        if (soldOutList.isEmpty()) {
            lblStatus.setText("🎉 Good news! No trips are currently sold out.");
            lblStatus.setStyle("-fx-text-fill: green;");
        } else {
            lblStatus.setText("⚠️ " + soldOutList.size() + " trips are fully booked and cannot accept new reservations.");
            lblStatus.setStyle("-fx-text-fill: orange;");
            listSoldOutTrips.setItems(FXCollections.observableArrayList(soldOutList));
        }
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}