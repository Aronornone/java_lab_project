package pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
public class Flight {
    private long flightId;
    private Airplane airplane;
    private String flightNumber;
    private Airport departureAirport;
    private Airport arrivalAirport;
    private Double baseCost;
    private int availablePlacesEconom;
    private int availablePlacesBusiness;
    private LocalDateTime dateTime;
    private Set<Ticket> tickets;

    private long departureAirportId;
    private long arrivalAirportId;

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
    public Flight(long id, Airplane airplane, String flightNumber, Airport departureAirport,
                  Airport arrivalAirport, Double baseCost, int availablePlacesEconom,
                  int availablePlacesBusiness, LocalDateTime dateTime) {
        this.flightId = id;
        this.airplane = airplane;
        this.flightNumber = flightNumber;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.baseCost = baseCost;
        this.availablePlacesEconom = availablePlacesEconom;
        this.availablePlacesBusiness = availablePlacesBusiness;
        this.dateTime = dateTime;
    }

    public Flight(long flightId, String flightNumber, Double baseCost, int availablePlacesEconom,
                  int availablePlacesBusiness, LocalDateTime dateTime, long departureAirportId, long arrivalAirportId) {
        this.flightId = flightId;
        this.flightNumber = flightNumber;
        this.baseCost = baseCost;
        this.dateTime = dateTime;
        this.availablePlacesEconom = availablePlacesEconom;
        this.availablePlacesBusiness = availablePlacesBusiness;
        this.departureAirportId = departureAirportId;
        this.arrivalAirportId = arrivalAirportId;
    }
}
