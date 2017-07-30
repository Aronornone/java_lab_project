package pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Flight {
    private long flightId;

    public Flight(Airplane airplane, String flightNumber, Airport departureAirport,
                  Airport arrivalAirport, Double baseCost, int availablePlacesEconom,
                  int availablePlacesBusiness, LocalDateTime dateTime) {
        this.airplane = airplane;
        this.flightNumber = flightNumber;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.baseCost = baseCost;
        this.availablePlacesEconom = availablePlacesEconom;
        this.availablePlacesBusiness = availablePlacesBusiness;
        this.dateTime = dateTime;
    }

    private Airplane airplane;
    private String flightNumber;
    private Airport departureAirport;
    private Airport arrivalAirport;
    private Double baseCost;
    private int availablePlacesEconom;
    private int availablePlacesBusiness;
    private LocalDateTime dateTime;
}
