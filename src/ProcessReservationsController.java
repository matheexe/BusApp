package src;

import javafx.fxml.FXML;

// Define the class ProcessReservationsController
public class ProcessReservationsController {
    
    // Placeholder for Bus object reference
    private Bus bus;

    public void setBus(Bus bus) {
        this.bus = bus;
        // Optionally print confirmation
        System.out.println("ProcessReservationsController received Bus data."); 
    }
    
}