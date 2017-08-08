package utils.generator;

import db.dao.DataSource;
import db.services.interfaces.FlightService;
import db.services.servicesimpl.FlightServiceImpl;
import lombok.SneakyThrows;
import pojo.Flight;

import java.sql.Statement;
import java.util.Collection;

public class FlightPlacesInit {
    private static FlightService fs = FlightServiceImpl.getInstance();

    public static void main(String[] args) {
        initFlightPlaces();
    }

    @SneakyThrows
    private static void initFlightPlaces() {
        Collection<Flight> flights = fs.getAll();
        StringBuilder query = new StringBuilder("INSERT INTO flightplace (flight_id,places_econom,places_business) VALUES ");
        query.append(getValues(flights));
        System.out.println(query.toString());
        Statement statement = DataSource.getConnection().createStatement();
        statement.executeUpdate(query.toString());
    }

    private static String getValues(Collection<Flight> flights) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Flight flight : flights) {
            String s = "(%d,'%s','%s')";
            stringBuilder.append(String.format(s, flight.getFlightId(), genEmptyPlaces(flight.getAvailablePlacesEconom()), genEmptyPlaces(flight.getAvailablePlacesBusiness())) + ',');
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);//delete the last ','
        return stringBuilder.toString();
    }

    private static String genEmptyPlaces(int numberOfPlaces) {
        if (numberOfPlaces == 0) return "NULL";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numberOfPlaces; i++) {
            sb.append(0);
        }
        return sb.toString();
    }
}
