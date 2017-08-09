package utils.generator;

import db.services.interfaces.FlightService;
import db.services.servicesimpl.FlightServiceImpl;
import lombok.SneakyThrows;
import pojo.Flight;
import pojo.OurBitSet;
import utils.ServletUtils;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static db.dao.DataSource.getConnection;

class FlightPlacesGeneratorPolina {

    private static final FlightService flightService = FlightServiceImpl.getInstance();

    public static void main(String[] args) throws SQLException {
        generateFlightPlaces();
    }

    @SneakyThrows
    private static void generateFlightPlaces() {
        List<Flight> flights = flightService.getAll();

        StringBuilder valueBuilder = new StringBuilder();
        for (Flight flight : flights) {
            valueBuilder.append("('").append(flight.getFlightId()).append("\',\'");
            int placesEconom = flight.getAirplane().getCapacityEconom()+1;
            int placesBusiness = flight.getAirplane().getCapacityBusiness()+1;
            OurBitSet bitSetEconom = new OurBitSet(placesEconom);
            OurBitSet bitSetBusiness = new OurBitSet(placesBusiness);
            valueBuilder.append(ServletUtils.stringConversionFromBitSet(bitSetEconom)).append("\',\'").append(ServletUtils.stringConversionFromBitSet(bitSetBusiness));
            valueBuilder.append("'),");
        }
        valueBuilder.deleteCharAt(valueBuilder.length() - 1);
        String value = valueBuilder.toString();

        String query = "INSERT INTO flightplace (flight_id, places_econom, places_business) VALUES " + value;
        Statement statement = getConnection().createStatement();
        statement.executeUpdate(query);

    }
}
