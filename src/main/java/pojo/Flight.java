package pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Flight {
    private long flightId;
    private Airplane airplane;
    private String flightNumber;
    private String departureAirport;
    private String arrivalAirport;
    private Double baseCost;
    private int numberUsedPlaces;
    private LocalDateTime dateTime;
}
