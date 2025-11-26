package src;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ReserveTripController {

    @FXML private TextField inputName;
    @FXML private TextField inputCPF;
    @FXML private TextField inputAge;
    @FXML private TextField inputEmail;
    @FXML private Label labelStatus;

    private Bus bus; // reference to BusApp.Bus object

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    @FXML
    private void handleConfirm() {

        try {
            String name = inputName.getText();
            String cpf = inputCPF.getText();
            int age = Integer.parseInt(inputAge.getText());
            String email = inputEmail.getText();

            Passenger p = new Passenger(name, cpf, age, email);

            boolean success = bus.bookSeat(p);

            if (success) {
                labelStatus.setText("Reservation completed!");
                labelStatus.setStyle("-fx-text-fill: green;");
            } else {
                labelStatus.setText("No seats available!");
                labelStatus.setStyle("-fx-text-fill: red;");
            }

        } catch (Exception e) {
            labelStatus.setText("Error: invalid or missing information.");
            labelStatus.setStyle("-fx-text-fill: red;");
        }
    }
}