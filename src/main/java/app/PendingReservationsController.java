package app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.Node;
import javafx.stage.Stage;

public class PendingReservationsController {

    private Bus bus;

    @FXML private ListView<String> listPending;

    public void setBus(Bus bus) {
        this.bus = bus;

        for (Passenger p : bus.getPendingReservations()) {
            listPending.getItems().add(p.name + " - " + p.cpf);
        }
    }

    @FXML
    private void handleClose(ActionEvent event) {
        // Fecha apenas a janela atual (Stage)
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}