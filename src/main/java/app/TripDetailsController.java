package app;


import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class TripDetailsController {

    private Bus bus;

    @FXML private Label lblBusInfo;
    @FXML private ListView<String> listPassengerDetails;

    public void setBus(Bus bus) {
        this.bus = bus;
        if (bus != null) {
            refreshDetails();
        } else {
            lblBusInfo.setText("No trip details available.");
        }
    }

    private void refreshDetails() {
        if (bus == null) return;
        
        // Exibe o resumo da viagem no topo
        lblBusInfo.setText(bus.getSummary());
        
        // Carrega a lista detalhada de passageiros
        listPassengerDetails.setItems(FXCollections.observableArrayList(bus.getAllPassengerDetails()));
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}