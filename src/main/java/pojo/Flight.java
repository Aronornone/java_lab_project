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

    public static class FlightBuilder {
        private long nestedFlightId;
        private Airplane nestedAirplane;
        private String nestedFlightNumber;
        private Airport nestedDepartureAirport;
        private Airport nestedArrivalAirport;
        private Double nestedBaseCost;
        private int nestedAvailablePlacesEconom;
        private int nestedAvailablePlacesBusiness;
        private LocalDateTime nestedDateTime;
        private Set<Ticket> nestedTickets;
        private long nestedDepartureAirportId;
        private long nestedArrivalAirportId;

        public FlightBuilder(long newFlightId, String newFlightNumber) {
            this.nestedFlightId = newFlightId;
            this.nestedFlightNumber = newFlightNumber;
        }

        public FlightBuilder departureAirport(Airport newAirport) {
            this.nestedDepartureAirport = newAirport;
            return this;
        }

        public FlightBuilder arrivalAirport(Airport newAirport) {
            this.nestedArrivalAirport = newAirport;
            return this;
        }

        public FlightBuilder departureAirportId(long newAirportId) {
            this.nestedDepartureAirportId = newAirportId;
            return this;
        }

        public FlightBuilder arrivalAirportId(long newAirportId) {
            this.nestedArrivalAirportId = newAirportId;
            return this;
        }

        public FlightBuilder airplane(Airplane newAirplane) {
            this.nestedAirplane = newAirplane;
            return this;
        }

        public FlightBuilder dateTime(LocalDateTime newDateTime) {
            this.nestedDateTime = newDateTime;
            return this;
        }

        public FlightBuilder baseCost(double newBaseCost) {
            this.nestedBaseCost = newBaseCost;
            return this;
        }

        public FlightBuilder availableEconom(int newEconomPlaces) {
            this.nestedAvailablePlacesEconom = newEconomPlaces;
            return this;
        }

        public FlightBuilder availableBusiness(int newBusinessPlaces) {
            this.nestedAvailablePlacesBusiness = newBusinessPlaces;
            return this;
        }

        public FlightBuilder setTickets(Set<Ticket> newTickets) {
            this.nestedTickets = newTickets;
            return this;
        }

        public Flight createFlight() {
            return new Flight(nestedFlightId, nestedAirplane, nestedFlightNumber,
                    nestedDepartureAirport, nestedArrivalAirport,
                    nestedBaseCost, nestedAvailablePlacesEconom, nestedAvailablePlacesBusiness,
                    nestedDateTime, nestedTickets, nestedDepartureAirportId, nestedArrivalAirportId);
        }
    }

}
