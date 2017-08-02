package utils;

import db.service.FlightPlaceService;
import db.service.FlightService;
import lombok.SneakyThrows;
import pojo.Flight;
import stubs.StubUtils;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static db.DataSource.getConnection;

public class FlightPlacesGeneratorPolina {

    public static void main(String[] args) throws SQLException {
        generateFlightPlaces();
    }

    @SneakyThrows
    private static void generateFlightPlaces() throws SQLException {
        FlightService fs = new FlightService();
        FlightPlaceService fps = new FlightPlaceService();
        List<Flight> flights = fs.getAll();
        int number = flights.size();

        StringBuilder valueBuilder = new StringBuilder();
        for (Flight flight : flights) {
            valueBuilder.append("('").append(flight.getFlightId()).append("\',\'");
            int placesEconom = flight.getAirplane().getCapacityEconom()+1;
            int placesBusiness = flight.getAirplane().getCapacityBusiness()+1;
            OurBitSet bitSetEconom = new OurBitSet(placesEconom);
            OurBitSet bitSetBusiness = new OurBitSet(placesBusiness);
            valueBuilder.append(StubUtils.stringConversionFromBitSet(bitSetEconom)).append("\',\'").append(StubUtils.stringConversionFromBitSet(bitSetBusiness));
            valueBuilder.append("'),");
        }
        valueBuilder.deleteCharAt(valueBuilder.length() - 1);
        String value = valueBuilder.toString();

        String query = "INSERT INTO flightplace (flight_id, places_econom, places_business) VALUES " + value;
        Statement statement = getConnection().createStatement();
        statement.executeUpdate(query);

    }
}
