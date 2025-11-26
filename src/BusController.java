package src;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class BusController {
    
    private Bus currentBus;
    
    // O Scanner para simular input de dados no console
    private static final Scanner scanner = new Scanner(System.in); 

    @FXML private Button btnBookTrip; // [1]
    @FXML private Button btnReservation; // [2]
    @FXML private Button btnCheckin; // [3]
    @FXML private Button btnSeat; // [4]
    @FXML private Button btnCancel; // [5]
    @FXML private Button btnAvailable; // [6]
    @FXML private Button btnSold; // [7]
    @FXML private Button btnPending; // [8]
    @FXML private Button btnDetails; // [9]
    @FXML private Button btnExit; // [10]

    // Injeção de Dependência do objeto Bus principal
    public void setBusData(Bus bus) {
        this.currentBus = bus;
        System.out.println("Sistema Pronto! Ônibus: " + bus.busNumber + " injetado no Controller.");
    }

    // ----------------------------------------------------
    // [1] Reservar Viagem (Usa a Fila)
    // ----------------------------------------------------

    @FXML
    public void handleBookTrip(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ReserveTrip.fxml"));
            Parent root = loader.load();

            ReserveTripController controller = loader.getController();
            controller.setBus(currentBus); // send nested Bus object

            Stage stage = new Stage();
            stage.setTitle("New Trip Reservation");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ----------------------------------------------------
    // [2] Process Pending Reservations (Admin)
    // ----------------------------------------------------

    @FXML
    public void handleProcessReservations(ActionEvent event) {
        System.out.println("\n--- [2] PROCESSING PENDING RESERVATIONS (Admin) ---");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProcessReservations.fxml"));
            Parent root = loader.load();

            ProcessReservationsController controller = loader.getController();
            controller.setBus(currentBus);

            Stage stage = new Stage();
            stage.setTitle("Process Reservations");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ----------------------------------------------------
    // [3] Check-in de Passageiros (Usa a Pilha)
    // ----------------------------------------------------

    @FXML
    public void handleCheckin(ActionEvent event) {
        System.out.println("\n--- [3] CHECK-IN (Modifica Estado) ---");
        System.out.print("Nome do Passageiro Confirmado: ");
    }
    
    // ----------------------------------------------------
    // [4] Alterar Assento
    // ----------------------------------------------------
    @FXML
    public void handleSeat(ActionEvent event) {
        System.out.println("\n--- [4] ALTERAR ASSENTO ---");
        System.out.print("Nome do Passageiro: ");
    }

    // ----------------------------------------------------
    // [5] Cancelar Passagem
    // ----------------------------------------------------
    @FXML
    public void handleCancel(ActionEvent event) {
        System.out.println("\n--- [5] CANCELAR PASSAGEM ---");
        System.out.print("Nome do Passageiro para cancelar: ");
    }
    
    // ----------------------------------------------------
    // [6] Consultar Viagens Disponíveis
    // ----------------------------------------------------
    @FXML
    public void handleAvailable(ActionEvent event) {
        System.out.println("\n--- [6] CONSULTA DISPONÍVEL ---");
    }
    
    // ----------------------------------------------------
    // [7] Consultar Viagens Esgotadas
    // ----------------------------------------------------
    @FXML
    public void handleSold(ActionEvent event) {
        System.out.println("\n--- [7] CONSULTA ESGOTADA ---");
    }

    // ----------------------------------------------------
    // [8] Consultar Reservas Pendentes (Fila)
    // ----------------------------------------------------
    @FXML
    public void handlePending(ActionEvent event) {
        System.out.println("\n--- [8] CONSULTA RESERVAS PENDENTES (FILA) ---");

    }

    // ----------------------------------------------------
    // [9] Consultar Detalhes de Viagem
    // ----------------------------------------------------
    @FXML
    public void handleDetails(ActionEvent event) {
        currentBus.displayTripInfo();
        
    }

    // ----------------------------------------------------
    // [10] Sair
    // ----------------------------------------------------
    @FXML
    public void handleExit(ActionEvent event) {
        System.out.println("Fechando sistema...");
        System.exit(0);
    }
}