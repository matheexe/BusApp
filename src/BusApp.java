package src;
import java.util.*; // Importing all util packages
import java.util.List; // Importing List from util package
import java.awt.*; // Importing all AWT packages 
import javafx.application.Application; // Importing JavaFX Application class
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene; // Importing JavaFX Scene class
import javafx.scene.control.Label; // Importing JavaFX Label class
import javafx.stage.Stage; // Importing JavaFX classes

public class BusApp extends Application{ // Main class extending Application for JavaFX

    @Override 
    public void start(Stage stage) throws Exception{ // Stage is the main window
        Parent root = FXMLLoader.load(getClass().getResource("BusView.fxml"));

        stage.setTitle("BusApp");
        stage.setScene(new Scene(root, 600, 400)); // Setting the scene to the stage
        stage.show(); 
    }

    public static void main(String[] args){ // Main method
        testBusLogic(); // Executa o teste manual antes da janela abrir
        launch(); 
    }

// function to test bus logic //
// Uncomment to run the test //
    public static void testBusLogic() {
        System.out.println("--- Iniciando Teste de Reserva ---");
        
         // 1. Criar um ônibus com rota e horario
        Bus bus = new Bus("BUS-TESTE", 2, "São Paulo", "Rio de Janeiro", "10:00");
        
        // 2. Criar 3 passageiros
        Passenger p1 = new Passenger("Ana");
        Passenger p2 = new Passenger("Bruno");
        Passenger p3 = new Passenger("Carlos");

        // 3. Tentar reservar assentos
    System.out.println("Tentando reservar para Ana:");
        bus.bookSeat(p1); // Esperado: Sucesso, Assento 1

    System.out.println("Tentando reservar para Bruno:");
        bus.bookSeat(p2); // Esperado: Sucesso, Assento 2

    System.out.println("Tentando reservar para Carlos (Ônibus cheio):");
        bus.bookSeat(p3); // Esperado: Falha (No seats available)
    
        bus.displayTripInfo();
        bus.listPassengers();

    System.out.println("--- Fim do Teste ---");
        
    }
}



// Lista para fazer:
// - Implementar funcionalidades para reservar, cancelar e verificar status de passagens | Maro menos pronta (funciona no console só)
// - Adicionar interface gráfica para interação do usuário | Em andamento
// - Implementar validação de dados e tratamento de erros | Em andamento
// - Impedir reserva ou check-in caso todos os assentos estejam ocupados | Em andamento
// - Exibir mensagens adequadas quando não houver assentos | funciona parcialmente até então (feito no console)
// - Função para configurar detalhes do ônibus, como número e capacidade | Em andamento
// - Função para gerenciar motoristas, incluindo nome e número da licença | Em andamento
// - Função para gerenciar passageiros/clientes, incluindo nome e número do assento | Em andamento
// - Função de consulta:
// - Exibir informações completas de uma viagem (ônibus, origem, destino, horários, capacidade, reservas confirmadas, check-ins realizados) | Em andamento
// - Exibir passageiros de uma viagem com status: pendente, confirmado, check-in | Parcialmente pronta (console)

enum State{
    PENDING, // Passenger has requested for ticket
    CONFIRMED, // Admin has confirmed the ticket
    CHECKIN, // Passenger has enterred the bus
    AWAITING // Trip is waiting to start
}

class Passenger{
    String name; //Passenger's name
    State state; // Passenger's current state
    int seat; // Passenger's seat number

    public Passenger(String name){
        this.name = name;
        this.state = State.PENDING; // Initial state is PENDING
        this.seat = -1; // Initial seat number is -1 (not assigned)
    }
}

class Driver{
    String name; // Driver's name
    String licenseNumber; // Driver's license number
    State state; // Driver's current state

    public Driver(String name, String licenseNumber){
        this.name = name;
        this.licenseNumber = licenseNumber;
        this.state = State.AWAITING; // Initial state is AWAITING
    }
}

class Bus{
    String busNumber; // Bus number
    String origin; // Origin of the bus
    String destination; // Destination of the bus
    String departureTime; // Departure time of the bus
    List<Passenger> passengers; // List of passengers
    int capacity; // Bus capacity

    public Bus(String busNumber, int capacity, String origin, String destination, String departureTime){
        this.busNumber = busNumber;
        this.capacity = capacity;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.passengers = new ArrayList<>();
    }

    public boolean bookSeat(Passenger passenger){
        if(passengers.size() >= capacity) {
            System.out.println(" No seats available.");
            return false; // No seats available
        }

        passenger.seat = passengers.size() + 1;
        passenger.state = State.CONFIRMED;
        passengers.add(passenger);
        System.out.println(" Seat booked successfully for " + passenger.name + " at seat number " + passenger.seat);
        return true; // Seat booked successfully   
    }   

    public boolean cancelSeat(Passenger passenger) {
        if (passengers.contains(passenger)) {
            passengers.remove(passenger);
            passenger.seat = -1;
            passenger.state = State.PENDING;
            System.out.println(" Seat cancellation successful for " + passenger.name);
            return true;
        }
        else{
            System.out.println(" No booking found for " + passenger.name);
            return false;
        }
    }

    public void displayTripInfo(){
        System.out.println("\n--- Trip Details ---");
        System.out.println("Bus Number: " + busNumber);
        System.out.println("Route: " + origin + " -> " + destination);
        System.out.println("Departure Time: " + departureTime);
        System.out.println("Ocupation: " + passengers.size() + "/" + capacity);
    }

    public void listPassengers(){
        System.out.println("\n--- Passenger List ---");
        if (passengers.isEmpty()) {
            System.out.println("No passengers booked.");
        }
        else{
            for (Passenger p : passengers) {
                System.out.println("Name: " + p.name + ", Seat: " + p.seat + ", Status: " + p.state);
            }
        }
    }
}

class Ticket{
    String ticketNumber; // Ticket number
    State state; // Current state of the ticket
    Passenger passenger; // Passenger associated with the ticket
    Bus bus; // Bus associated with the ticket
}

class Seat{
    boolean isAvailable; // Availability of the seat
    Passenger passenger; // Passenger assigned to the seat
    int seatNumber; // Seat number
}