package src;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
        System.out.println("\n--- [1] NOVA RESERVA (PENDENTE) ---");
        System.out.print("Nome: ");
        String name = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        System.out.print("Idade: ");
        int age = scanner.nextInt();
        scanner.nextLine(); 
        System.out.print("E-mail válido: ");
        String email = scanner.nextLine();
        
        // Simples validação de capacidade para evitar fila desnecessária

        Passenger p = new Passenger(name, cpf, age, email);
        System.out.println("✅ Reserva de " + name + " na FILA de espera. Status: PENDING.");
    }

    // ----------------------------------------------------
    // [2] Processar Reservas (Admin - Remove da Fila, Adiciona à Lista)
    // ----------------------------------------------------
    @FXML
    public void handleReservation(ActionEvent event) { 
        System.out.println("\n--- [2] PROCESSANDO RESERVAS PENDENTES (Admin) ---");
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