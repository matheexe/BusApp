package src;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TripScheduler {
    
    // Lista principal que armazena todas as viagens
    private List<Bus> availableTrips = new ArrayList<>();

    public TripScheduler() {
        // Inicializa com dados de teste para que o usuário possa escolher
        initializeTrips();
    }

    private void initializeTrips() {
        // Exemplo 1: Viagem SP -> RJ
        Bus trip1 = new Bus("TRIP0001", 40, "São Paulo", "Rio de Janeiro", "08:00");
        availableTrips.add(trip1);
        
        // Exemplo 2: Viagem SP -> Curitiba
        Bus trip2 = new Bus("TRIP0002", 50, "São Paulo", "Curitiba", "14:30");
        availableTrips.add(trip2);
        
        // Exemplo 3: Viagem RJ -> Belo Horizonte
        Bus trip3 = new Bus("TRIP0003", 30, "Rio de Janeiro", "Belo Horizonte", "22:00");
        availableTrips.add(trip3);
    }
    
    // Método para obter todas as viagens disponíveis em formato de string
    public List<Bus> getAvailableTrips() {
        return availableTrips; 
    }

    // Método para encontrar um ônibus pelo seu número
    public Bus getTripByNumber(String busNumber) {
        return availableTrips.stream()
                .filter(bus -> bus.busNumber.equals(busNumber))
                .findFirst()
                .orElse(null);
    }

    public List<String> getAllTripSummaries() {
        return availableTrips.stream()
                .map(Bus::getSummary) 
                .collect(java.util.stream.Collectors.toList());
    }

    public List<String> getSoldOutTrips() {
    return availableTrips.stream()
        .filter(bus -> bus.getActiveReservationCount() >= bus.capacity) 
        .map(Bus::getSummary) 
        .collect(java.util.stream.Collectors.toList());
    }
}