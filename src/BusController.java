package src;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import src.TripScheduler; // Certifique-se de que este import está correto!

public class BusController {

    // --- CAMPOS DE DADOS E SCHEDULER ---
    private TripScheduler scheduler; 
    private Bus selectedBus; // Variável principal para todas as operações

    // @FXML private Label lblCurrentTrip; // Recomendado adicionar no FXML para feedback visual
    
    @FXML private Button btnBookTrip;
    @FXML private Button btnReservation;
    @FXML private Button btnCheckin;
    @FXML private Button btnSeat;
    @FXML private Button btnCancel;
    @FXML private Button btnAvailable; // Este botão deve ser conectado ao handleOpenTripSelection
    @FXML private Button btnSold;
    @FXML private Button btnPending;
    @FXML private Button btnDetails;
    @FXML private Button btnExit;
    
    // Antigo setBusData(Bus bus) será descontinuado/ignorado na nova arquitetura.
    // public void setBusData(Bus bus) { ... } 
    

    // --- MÉTODOS DE CONTROLE DO SCHEDULER ---

    public void setTripScheduler(TripScheduler scheduler) {
        this.scheduler = scheduler;
        
        // Inicializa com o primeiro ônibus disponível, se houver.
        if (!scheduler.getAvailableTrips().isEmpty()) {
            setSelectedBus(scheduler.getAvailableTrips().get(0)); 
        } else {
            setSelectedBus(null); 
        }
    }

    public void setSelectedBus(Bus bus) {
        this.selectedBus = bus;
        
        // Atualiza a UI principal (se você tiver um Label para isso)
        if (bus != null) {
            System.out.println("BusController: Trip " + bus.busNumber + " is now selected.");
            // if (lblCurrentTrip != null) lblCurrentTrip.setText("Current Trip: " + bus.getSummary());
        } else {
            System.out.println("BusController: No trip is currently selected.");
            // if (lblCurrentTrip != null) lblCurrentTrip.setText("No trip selected. Select one first.");
        }
    }
    
    // --- LÓGICA DE ABERTURA DE JANELA REUTILIZÁVEL ---

    /**
     * Verifica o ônibus selecionado e abre a janela FXML, passando o selectedBus.
     */
    private void openWindow(String fxml, String title, boolean requiresSelectedBus) {
        if (requiresSelectedBus && selectedBus == null) {
            System.out.println("[ERROR] No trip selected. Please select a trip first.");
            // Aqui você pode adicionar um Alert Box para o usuário.
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            Object controller = loader.getController();

            // Tenta injetar o selectedBus no controller da nova janela
            try {
                // Passamos o SELECTED BUS (que pode ser null se requiresSelectedBus=false)
                controller.getClass().getMethod("setBus", Bus.class)
                    .invoke(controller, selectedBus); 
            } catch (NoSuchMethodException ignored) {
                // Se o método setBus não existe, apenas ignora.
            }

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- HANDLERS (AGORA USANDO selectedBus) ---

    @FXML
    public void handleBookTrip(ActionEvent event) {
        // Requer um ônibus selecionado para reservar
        openWindow("ReserveTrip.fxml", "Book a Trip", true); 
    }

    @FXML
    public void handleProcessReservations(ActionEvent event) {
        // Requer um ônibus selecionado
        openWindow("ProcessReservations.fxml", "Process Reservations", true); 
    }

    @FXML
    public void handleCheckin(ActionEvent event) {
        // Requer um ônibus selecionado
        openWindow("CheckIn.fxml", "Passenger Check-in", true); 
    }

    @FXML
    public void handleSeat(ActionEvent event) {
        // Requer um ônibus selecionado
        openWindow("ChangeSeat.fxml", "Change Seat", true); 
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        // Requer um ônibus selecionado
        openWindow("CancelTicket.fxml", "Cancel Ticket", true); 
    }
    
    // O botão 'AvailableTrips' agora abre a TELA DE SELEÇÃO DE VIAGEM!
    @FXML
    public void handleAvailable(ActionEvent event) {
        handleOpenTripSelection(event);
    }

    @FXML
    public void handleSold(ActionEvent event) {
        if (scheduler == null) {
            System.out.println("[ERROR] Scheduler is not initialized.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SoldOutTrips.fxml"));
            Parent root = loader.load();

            SoldOutTripsController controller = loader.getController();
            
            // Passa o scheduler para o novo controller
            controller.setScheduler(scheduler); 

            Stage stage = new Stage();
            stage.setTitle("Sold Out Trips");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handlePending(ActionEvent event) {
        System.out.println("Pending Reservations listing functionality not yet implemented.");
    }

    @FXML
    public void handleDetails(ActionEvent event) {
        // Requer um ônibus selecionado para ver os detalhes
        openWindow("TripDetails.fxml", "View Trip Details", true); 
    }
    
    @FXML
    private void handleOpenTripSelection(ActionEvent event) {
        if (scheduler == null) {
            System.out.println("Scheduler is not initialized.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TripSelection.fxml"));
            Parent root = loader.load();

            TripSelectionController controller = loader.getController();
            // Passa o scheduler e ele mesmo (this) para que o controller de seleção possa
            // retornar o ônibus escolhido para setSelectedBus()
            controller.setScheduler(scheduler, this); 

            Stage stage = new Stage();
            stage.setTitle("Select Available Trip");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleExit(ActionEvent event) {
        System.out.println("Shutting down system...");
        System.exit(0);
    }
}