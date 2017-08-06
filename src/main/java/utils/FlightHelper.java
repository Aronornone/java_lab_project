package utils;

import lombok.Data;
import pojo.Airport;

import java.time.LocalDateTime;

@Data
public class FlightHelper {
    private long flightId;
    private Double baseCost;
    private String flightNumber;
    private long departureAirport;
    private long arrivalAirport;
    private LocalDateTime dateTime;
    private Airport departureAir;
    private Airport arrivalAir;

    public FlightHelper(long flightId, Double baseCost, String flightNumber, long departureAirport, long arrivalAirport, LocalDateTime dateTime) {
        this.flightId = flightId;
        this.baseCost = baseCost;
        this.flightNumber = flightNumber;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "FlightHelper{" +
                "flightId=" + flightId +
                ", baseCost=" + baseCost +
                ", flightNumber='" + flightNumber + '\'' +
                ", arrivalAirport='" + arrivalAirport + '\'' +
                ", departureAirport='" + departureAirport + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}
