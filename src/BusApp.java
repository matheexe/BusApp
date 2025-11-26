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

    @Override public void start(Stage stage) throws Exception { 
        // 1. Criar o carregador
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BusView.fxml"));
        
        // 2. Carregar a hierarquia (View)
        Parent root = loader.load();

        // 3. Pegar o Controller que o FXML criou automaticamente
        BusController controller = loader.getController();

        // 4. Criar os dados reais (O Ônibus Principal)
        Bus meuOnibus = new Bus("VIACAO-JAVA", 40, "São Paulo", "Curitiba", "14:30");
        
        // 5. PASSAR os dados para o controller (A PONTE!)
        controller.setBusData(meuOnibus);

        // Configuração padrão do palco
        stage.setTitle("BusApp - Sistema de Reservas");
        stage.setScene(new Scene(root, 1280, 720)); 
        stage.show();
    }

    public static void main(String[] args){ // Main method
        testBusLogic(); // Executa o teste manual antes da janela abrir
        launch(); 
    }

// Function to test bus logic //
    public static void testBusLogic() {
        System.out.println("--- Iniciando Teste de Reserva ---");
        
         // 1. Criar um ônibus com rota e horario
        Bus bus = new Bus("BUS-TESTE", 2, "São Paulo", "Rio de Janeiro", "10:00");
        
        // 2. Criar 3 passageiros
        Passenger p1 = new Passenger("Ana Silva", "123.456.789-00", 30, "ana@email.com"); 
        Passenger p2 = new Passenger("Luiz Santos", "987.654.321-00", 45, "luiz@email.com"); 
        Passenger p3 = new Passenger("Maria Oliveira", "111.222.333-44", 22, "maria@email.com"); 

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

enum State {
    PENDING,     
    CONFIRMED,   
    CHECKEDIN,
    AWAITING,  
    CANCELED
}

class Passenger{
    public String name;
    public String cpf;
    public String email;
    public State state; 
    public int age;
    public int seat; 

    public Passenger(String name, String cpf, int age, String email) { 
        this.name = name;
        this.cpf = cpf;
        this.age = age;
        this.email = email;
        this.state = State.PENDING; 
        this.seat = -1; 
    }
    
    @Override
    public String toString() {
        return "Nome: " + name + ", Assento: " + (seat == -1 ? "N/A" : seat) + ", Status: " + state;
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

class Bus {

    String busNumber;
    String origin;
    String destination;
    String departureTime;
    int capacity;

    // Confirmed passengers
    List<Passenger> passengers = new ArrayList<>();

    // Pending reservations
    Queue<Passenger> pendingReservations = new LinkedList<>();

    // Stack for check-in
    Stack<Passenger> checkInStack = new Stack<>();

    // Vector for seats
    Passenger[] seats;

    public Bus(String busNumber, int capacity, String origin, String destination, String departureTime) {
        this.busNumber = busNumber;
        this.capacity = capacity;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.seats = new Passenger[capacity];
    }

    // ---------------------------------------------------
    // 1. BOOK TRIP  (Confirm or add to pending queue)
    // ---------------------------------------------------
    public boolean bookSeat(Passenger passenger) {
        // If seats are full → add to pending queue
        if (passengers.size() >= capacity) {
            passenger.state = State.PENDING;
            pendingReservations.add(passenger);
            System.out.println("No seats available. Added to pending reservations.");
            return false;
        }

        // Confirm reservation
        passenger.seat = passengers.size() + 1;
        passenger.state = State.CONFIRMED;
        seats[passenger.seat - 1] = passenger;
        passengers.add(passenger);

        System.out.println("Seat booked for " + passenger.name + " (Seat " + passenger.seat + ")");
        return true;
    }

    // ---------------------------------------------------
    // 2. PROCESS NEXT PENDING RESERVATION (Queue → Seat)
    // ---------------------------------------------------
    public Passenger processNextReservation() {
        if (pendingReservations.isEmpty()) return null;
        if (passengers.size() >= capacity) return null;

        Passenger p = pendingReservations.poll();

        p.seat = passengers.size() + 1;
        p.state = State.CONFIRMED;

        seats[p.seat - 1] = p;
        passengers.add(p);

        return p;
    }

    // ---------------------------------------------------
    // 3. CHECK-IN (Stack)
    // ---------------------------------------------------
    public boolean checkInPassenger(String cpf) {
        for (Passenger p : passengers) {
            if (p.cpf.equals(cpf)) {
                if (p.state == State.CHECKEDIN) return false;
                p.state = State.CHECKEDIN;
                checkInStack.push(p);
                return true;
            }
        }
        return false;
    }

    // ---------------------------------------------------
    // 4. CHANGE SEAT
    // ---------------------------------------------------
    public boolean changeSeat(String cpf, int newSeat) {
        if (newSeat < 1 || newSeat > capacity) return false;
        if (seats[newSeat - 1] != null) return false;

        for (Passenger p : passengers) {
            if (p.cpf.equals(cpf)) {
                seats[p.seat - 1] = null;
                p.seat = newSeat;
                seats[newSeat - 1] = p;
                return true;
            }
        }
        return false;
    }

    // ---------------------------------------------------
    // 5. CANCEL RESERVATION
    // ---------------------------------------------------
    public boolean cancelReservation(String cpf) {
        for (Passenger p : passengers) {
            if (p.cpf.equals(cpf)) {

                seats[p.seat - 1] = null;
                passengers.remove(p);
                p.state = State.CANCELED;
                p.seat = -1;

                // Try processing pending
                if (!pendingReservations.isEmpty()) {
                    processNextReservation();
                }

                return true;
            }
        }
        return false;
    }

    // ---------------------------------------------------
    // 6. SHOW AVAILABLE TRIPS
    // ---------------------------------------------------
    public boolean isAvailable() {
        return passengers.size() < capacity;
    }

    // ---------------------------------------------------
    // 7. SHOW SOLD-OUT TRIPS
    // ---------------------------------------------------
    public boolean isSoldOut() {
        return passengers.size() >= capacity;
    }

    // ---------------------------------------------------
    // 8. LIST PENDING RESERVATIONS
    // ---------------------------------------------------
    public List<Passenger> getPendingReservations() {
        return new ArrayList<>(pendingReservations);
    }

    // ---------------------------------------------------
    // 9. SHOW FULL TRIP DETAILS
    // ---------------------------------------------------
    public void printTripDetails() {
        System.out.println("Bus: " + busNumber);
        System.out.println("Route: " + origin + " → " + destination);
        System.out.println("Departure: " + departureTime);
        System.out.println("Capacity: " + capacity);
        System.out.println("Confirmed: " + passengers.size());
        System.out.println("Check-ins: " + checkInStack.size());
        System.out.println("Pending: " + pendingReservations.size());
    }

    public void displayTripInfo() {
        System.out.println("----- TRIP INFO -----");
        System.out.println("Destination: " + destination);
        System.out.println("Origin: " + origin);
        System.out.println("Capacity: " + capacity);
        System.out.println("Reserved seats: " + passengers.size()); 
    }

    public void listPassengers() {
        System.out.println("----- PASSENGERS -----");
        for (Passenger p : passengers) { 
            System.out.println(p.name + " - " + p.cpf); 
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