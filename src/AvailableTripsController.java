package src;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.util.Map;

// CLASSE RENOMEADA
public class AvailableTripsController {

    private Bus bus;

    @FXML private ListView<String> listStatus;

    public void setBus(Bus bus) {
        this.bus = bus;
        refreshStatus();
    }

    private void refreshStatus() {
        if (bus == null) return;
        
        listStatus.getItems().clear();
        
        // Pega o mapa de status da classe Bus
        Map<String, String> statusMap = bus.getFullTripStatus();
        
        for (Map.Entry<String, String> entry : statusMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            
            if (value.isEmpty()) {
                // Para separadores como "--- PASSENGER STATUS ---"
                listStatus.getItems().add(key);
            } else {
                // Para os pares chave: valor
                listStatus.getItems().add(key + ": " + value);
            }
        }
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}