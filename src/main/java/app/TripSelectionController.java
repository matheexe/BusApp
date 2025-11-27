package app;


import java.util.List;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class TripSelectionController {

    private TripScheduler scheduler;
    private BusController mainController; // Referência ao controller principal
    
    @FXML private ListView<String> listTrips;
    @FXML private Label lblStatus;

    /**
     * Recebe o scheduler e o controller principal para feedback.
     */
    public void setScheduler(TripScheduler scheduler, BusController mainController) {
        this.scheduler = scheduler;
        this.mainController = mainController;
        
        // Carrega a lista de resumos de viagens
        listTrips.setItems(FXCollections.observableArrayList(scheduler.getAllTripSummaries()));
        
        lblStatus.setText("Select a trip from the list to manage.");
    }

    @FXML
    private void handleSelectTrip(ActionEvent event) {
        String selectedSummary = listTrips.getSelectionModel().getSelectedItem();
        
        if (selectedSummary == null) {
            lblStatus.setText("❌ Please select a trip first.");
            lblStatus.setStyle("-fx-text-fill: red;");
            return;
        }

        // Extrai o número da viagem (Bus Number)
        // Assume que o número do ônibus está no início da string do resumo (ex: "Bus TRIP001 |...")
        String[] parts = selectedSummary.split("\\|");
        String busNumber = parts[0].trim().replace("Bus ", "");
        
        // Pega o objeto Bus completo
        Bus selectedBus = scheduler.getTripByNumber(busNumber);

        if (selectedBus != null) {
            // 4. Notifica o controller principal com o novo ônibus selecionado
            mainController.setSelectedBus(selectedBus); 
            
            lblStatus.setText("✅ Trip " + busNumber + " selected successfully!");
            lblStatus.setStyle("-fx-text-fill: green;");
            
            // Fecha a janela de seleção
            handleClose(event);
        } else {
            lblStatus.setText("⚠️ Error finding the selected trip data.");
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