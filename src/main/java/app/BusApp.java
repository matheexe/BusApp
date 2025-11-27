package app;

import java.util.ArrayList; // Importing all util packages
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List; // Importing List from util package
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.stream.Collectors;
import javafx.application.Application; // Importing JavaFX Application class
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene; // Importing JavaFX Scene class
import javafx.stage.Stage; // Importing JavaFX classes
import java.awt.*; // Importing all AWT packages 
import javafx.scene.control.Label; // Importing JavaFX Label class

public class BusApp extends Application{ // Main class extending Application for JavaFX

    
    private TripScheduler scheduler;
    @Override public void start(Stage stage) throws Exception { 
    
    this.scheduler = new TripScheduler(); 
    
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/BusView.fxml"));
    
    Parent root = loader.load();

    BusController controller = loader.getController();

    controller.setTripScheduler(this.scheduler); 

    stage.setTitle("BusApp!");
    stage.setScene(new Scene(root, 1280, 720)); 
    stage.show();
    }

    public static void main(String[] args){ // Main method
        testBusLogic(); // Executes the manual test before the window opens
        launch(); 
    }

    // Function to test bus logic //
    public static void testBusLogic() {
        System.out.println("--- Starting Reservation Test ---");
        
            // 1. Create a bus with route and schedule
        Bus bus = new Bus("BUS-TESTE", 2, "São Paulo", "Rio de Janeiro", "10:00");
        
        // 2. Create 3 passengers
        Passenger p1 = new Passenger("Ana Silva", "123.456.789-00", 30, "ana@email.com"); 
        Passenger p2 = new Passenger("Luiz Santos", "987.654.321-00", 45, "luiz@email.com"); 
        Passenger p3 = new Passenger("Maria Oliveira", "111.222.333-44", 22, "maria@email.com"); 

        // 3. Attempt to book seats
    System.out.println("Attempting to book for Ana:");
        bus.bookSeat(p1); // Expected: Success, Seat 1

    System.out.println("Attempting to book for Bruno:");
        bus.bookSeat(p2); // Expected: Success, Seat 2

    System.out.println("Attempting to book for Carlos (Bus full):");
        bus.bookSeat(p3); // Expected: Failure (No seats available)
    
        bus.displayTripInfo();
        bus.listPassengers();

    System.out.println("--- End of Test ---");
        
    }
}

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
        return "Name: " + name + ", Seat: " + (seat == -1 ? "N/A" : seat) + ", Status: " + state;
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
    private Stack<Passenger> checkInStack = new Stack<>();

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

    public boolean bookSeat(Passenger passenger) {
    
        // 1. Duplicity Validation (CPF)
        if (isPassengerDuplicate(passenger.cpf)) { 
            System.out.println("Reservation failed: Passenger with CPF " + passenger.cpf + " already has a reservation.");
            return false;
        }

        passenger.state = State.PENDING; 
        pendingReservations.add(passenger);
        
        System.out.println("Reservation request placed in pending queue for manual approval.");
        return true;
    }

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

    public boolean checkInPassenger(String cpf) {
        for (Passenger p : passengers) {
            if (p.cpf.equals(cpf)) {
                
                // Checks if the passenger has already checked-in
                if (p.state == State.CHECKEDIN) {
                    System.out.println("Check-in failed: Passenger already checked in.");
                    return false;
                }
                
                // 2. Checks if the state is CONFIRMED (only confirmed can board)
                if (p.state != State.CONFIRMED) { 
                    System.out.println("Check-in failed: Reservation is not confirmed (Status: " + p.state + ").");
                    return false; 
                }
                
                // 3. Occupies the physical seat on the bus
                // WE ASSUME that 'seats' is a Passenger[] array and 'p.seat' is the seat number (1 to N).
                // The array index is (p.seat - 1).
                if (p.seat > 0 && p.seat <= capacity) { // Assuming 'capacity' is accessible here
                    seats[p.seat - 1] = p;
                } else {
                    System.out.println("Error: Confirmed reservation has an invalid seat number.");
                    return false; 
                }
                
                // 4. Updates the state and Adds to the Stack (Stack)
                p.state = State.CHECKEDIN;
                checkInStack.push(p); // LIFO
                
                System.out.println("Check-in successful for " + p.name + " (Seat " + p.seat + ").");
                return true;
            }
        }
        
        // 5. Passenger not found
        System.out.println("Check-in failed: Passenger not found.");
        return false;
    }

    public List<String> getCheckedInPassengers() {
        List<String> list = new ArrayList<>();
        
        for (Passenger p : checkInStack) {
            list.add("Seat " + p.seat + ": " + p.name + " (Status: CHECKEDIN)"); 
        }
        return list;
    }

    public boolean isPassengerDuplicate(String cpf) {
        for (Passenger p : passengers) {
            if (p.cpf.equals(cpf)) {
                return true;
            }
        }
        
        for (Passenger p : pendingReservations) {
            if (p.cpf.equals(cpf)) {
                return true;
            }
        }
        
        return false;
    }

    public int getActiveReservationCount() {
        return passengers.size();
    }

    public boolean rejectPendingReservation(String cpf) {
    Iterator<Passenger> iterator = pendingReservations.iterator();
    
        while (iterator.hasNext()) {
            Passenger p = iterator.next();
            if (p.cpf.equals(cpf)) {
                iterator.remove(); 
                p.state = State.CANCELED; 
                return true; 
            }
        }
        return false; 
    }

    public List<String> getCheckInList() {
        List<String> list = new ArrayList<>();
        for (Passenger p : checkInStack) {
            list.add(p.name + " (Seat " + p.seat + ")");
        }
        return list;
    }

    public boolean isSeatAvailable(int seat) {
        if (seat < 1 || seat > capacity) return false;
        return seats[seat - 1] == null;
    }

    public List<String> getSeatMap() {
        List<String> map = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            if (seats[i] == null)
                map.add("Seat " + (i + 1) + ": FREE");
            else
                map.add("Seat " + (i + 1) + ": " + seats[i].name);
        }
        return map;
    }

    public boolean changeSeat(String cpf, int newSeatNumber) {
        if (newSeatNumber <= 0 || newSeatNumber > capacity) {
            System.out.println("Seat Change Failed: Invalid new seat number (" + newSeatNumber + ").");
            return false;
        }

        // 1. Find the passenger (targetPassenger)
        Passenger targetPassenger = null;
        for (Passenger p : passengers) {
            if (p.cpf.equals(cpf)) {
                targetPassenger = p;
                break;
            }
        }

        if (targetPassenger == null || targetPassenger.state == State.PENDING || targetPassenger.state == State.CANCELED) {
            System.out.println("Seat Change Failed: Passenger not found or reservation not confirmed/checked-in.");
            return false;
        }
        
        // Check if the passenger is already in the requested seat
        if (targetPassenger.seat == newSeatNumber) {
            System.out.println("Seat Change Info: Passenger is already in seat " + newSeatNumber);
            return true; 
        }

        // 2. Check if the new seat is occupied by another active passenger
        // Fixing the 'effectively final' error (Line 309, 331)
        
        // We declare a final variable that holds the CPF of the target passenger.
        // Using the CPF inside the stream ensures that the targetPassenger object (which is not final) is not referenced directly.
        final String targetCpf = targetPassenger.cpf;
        
        // Now the stream can be used:
        boolean isNewSeatOccupied = passengers.stream()
                // Filters by active passengers who are in the new seat.
                .filter(p -> p.seat == newSeatNumber && p.state != State.CANCELED)
                // And checks if the CPF of this found passenger IS NOT the CPF of our target passenger.
                // This avoids direct reference to the targetPassenger object, solving the 'effectively final' problem.
                .anyMatch(p -> !p.cpf.equals(targetCpf)); 

        if (isNewSeatOccupied) {
            System.out.println("Seat Change Failed: Seat " + newSeatNumber + " is already reserved or occupied.");
            return false;
        }
        
        // 3. Process the change
        int oldSeatNumber = targetPassenger.seat;

        if (targetPassenger.state == State.CHECKEDIN) {
            // Frees the old seat ONLY in the physical vector, if the passenger boarded
            seats[oldSeatNumber - 1] = null; 
        }

        // Updates the seat in the Passenger object
        targetPassenger.seat = newSeatNumber;

        if (targetPassenger.state == State.CHECKEDIN) {
            // Occupies the new seat ONLY in the physical vector, if the passenger boarded
            seats[newSeatNumber - 1] = targetPassenger; 
        }

        System.out.println("Seat successfully changed for " + targetPassenger.name + 
            " from seat " + oldSeatNumber + " to seat " + newSeatNumber + ".");
        return true;
    }

    public boolean cancelReservation(String cpf) {
        // 1. Find the passenger in the active list (Confirmed/CheckedIn)
        Passenger targetPassenger = null;
        Iterator<Passenger> iterator = passengers.iterator();
        while (iterator.hasNext()) {
            Passenger p = iterator.next();
            
            // Checks if the CPF matches and if the reservation is active (not CANCELED, not PENDING)
            if (p.cpf.equals(cpf) && (p.state == State.CONFIRMED || p.state == State.CHECKEDIN)) {
                targetPassenger = p;
                break;
            }
        }

        if (targetPassenger == null) {
            System.out.println("Cancellation failed: Active reservation not found for CPF " + cpf + ".");
            return false;
        }
        
        // 2. Liberate the seat
        int seatNumber = targetPassenger.seat;
        
        // The seat must be > 0 to be freed in the vector
        if (seatNumber > 0 && seatNumber <= capacity) {
            seats[seatNumber - 1] = null; // Frees the physical seat
        }
        
        // 3. Remove passenger from the main lists and update state
        iterator.remove(); // Remove from 'passengers' list
        
        // If the passenger was in the check-in stack (only if CHECKEDIN), remove.
        if (targetPassenger.state == State.CHECKEDIN) {
            // Note: The Stack is difficult to iterate and remove an item from the middle.
            // The simplest and safest way to perform removal of a non-top element in a Stack/List is:
            checkInStack.remove(targetPassenger);
        }
        
        targetPassenger.state = State.CANCELED;
        targetPassenger.seat = -1; // Invalid seat
        
        System.out.println("Reservation successfully canceled for " + targetPassenger.name + " (Seat " + seatNumber + ").");

        // 4. Try processing the next pending reservation
        // Tries to approve the next pending reservation, taking advantage of the freed seat.
        if (!pendingReservations.isEmpty()) {
            Passenger approved = processNextReservation(); // processNextReservation already assigns the correct seat
            if (approved != null) {
                System.out.println("INFO: Pending reservation approved automatically for " + approved.name + " (Seat " + approved.seat + ").");
            }
        }

        return true;
    }

    public boolean isAvailable() {
        return passengers.size() < capacity;
    }

    public boolean isSoldOut() {
        return passengers.size() >= capacity;
    }

    public List<Passenger> getPendingReservations() {
        return new ArrayList<>(pendingReservations);
    }

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

    public boolean hasPendingOrConfirmed(String cpf) {
        for (Passenger p : passengers) {
            if (p.cpf.equals(cpf)) return true;
        }
        return false;
    }

    public List<String> getPendingReservationNames() {
        List<String> list = new ArrayList<>();
        for (Passenger p : pendingReservations) {
            list.add(p.name);
        }
        return list;
    }

    public boolean approveReservation(String name) {

        // Find passenger in queue
        Passenger target = null;
        for (Passenger p : pendingReservations) {
            if (p.name.equals(name)) {
                target = p;
                break;
            }
        }

        if (target == null) return false;

        // Cannot approve if bus is full
        if (passengers.size() >= capacity) return false;

        // Remove from queue
        pendingReservations.remove(target);

        // Confirm reservation
        target.seat = passengers.size() + 1;
        target.state = State.CONFIRMED;
        seats[target.seat - 1] = target;
        passengers.add(target);

        return true;
    }

    public boolean rejectReservation(String name) {

        Passenger target = null;

        for (Passenger p : pendingReservations) {
            if (p.name.equals(name)) {
                target = p;
                break;
            }
        }

        if (target == null) return false;

        pendingReservations.remove(target);
        target.state = State.CANCELED;

        return true;
    }

    public List<String> getPassengerStatusList() {
        List<String> list = new ArrayList<>();
        for (Passenger p : passengers) {
            list.add(p.name + " - " + p.cpf + " - " + p.state);
        }
        for (Passenger p : pendingReservations) {
            list.add(p.name + " - " + p.cpf + " - PENDING");
        }
        return list;
    }

    public Map<String, String> getFullTripStatus() {
        Map<String, String> status = new LinkedHashMap<>();

        // Trip Details
        status.put("Bus Number", busNumber);
        status.put("Route", origin + " → " + destination);
        status.put("Departure Time", departureTime);
        status.put("Total Capacity", String.valueOf(capacity));
        
        // Passenger Count and Status
        int confirmedCount = 0;
        int checkInCount = 0;
        
        for (Passenger p : passengers) {
            if (p.state == State.CONFIRMED) {
                confirmedCount++;
            }
            if (p.state == State.CHECKEDIN) {
                checkInCount++;
            }
        }
        
        int availableSeats = capacity - passengers.size();
        
        status.put("--- PASSENGER STATUS ---", "");
        status.put("Total Active Reservations", String.valueOf(passengers.size()));
        status.put("Available Seats (Remaining)", String.valueOf(availableSeats));
        status.put("Pending Reservations (Queue)", String.valueOf(pendingReservations.size()));
        status.put("Confirmed (No Check-in)", String.valueOf(confirmedCount));
        status.put("Checked-In (Boarded)", String.valueOf(checkInCount));
        
        return status;
    }

    public String getSummary() {
        int reserved = passengers.size();
        int available = capacity - reserved;
        return String.format(
            "Bus %s | %s → %s | %s | Seats: %d/%d Available", 
            busNumber, origin, destination, departureTime, available, capacity
        );
    }

    public String getFullTripDetails() {
        return "Bus: " + busNumber + "\n" +
            "Route: " + origin + " → " + destination + "\n" +
            "Departure: " + departureTime + "\n" +
            "Capacity: " + capacity + "\n" +
            "Confirmed: " + passengers.size() + "\n" +
            "Check-ins: " + checkInStack.size() + "\n" +
            "Pending: " + pendingReservations.size();
    }

    public List<String> getAllPassengerDetails() {
        List<String> details = new ArrayList<>();
        
        details.add("--- ACTIVE RESERVATIONS (" + passengers.size() + ") ---");
        if (passengers.isEmpty()) {
            details.add("No confirmed or checked-in passengers.");
        } else {
            List<Passenger> sortedPassengers = passengers.stream()
                        .sorted(Comparator.comparingInt(p -> p.seat))
                        .collect(Collectors.toList());
                        
            for (Passenger p : sortedPassengers) {
                details.add(String.format("Seat %d: %s (CPF: %s) - State: %s", 
                p.seat, p.name, p.cpf, p.state));
            }
        }
        
        details.add("\n--- PENDING RESERVATIONS (" + pendingReservations.size() + ") ---");
        if (pendingReservations.isEmpty()) {
            details.add("No pending reservations.");
        } else {

            for (Passenger p : pendingReservations) {
                details.add(String.format("Queue: %s (CPF: %s) - State: %s", 
                p.name, p.cpf, p.state));
            }
        }
        
        return details;
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