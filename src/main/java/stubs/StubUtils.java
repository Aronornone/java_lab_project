package stubs;

import db.DataSource;
import db.service.AirplaneService;
import db.service.AirportService;
import db.service.FlightPlaceService;
import db.service.FlightService;
import pojo.Airplane;
import pojo.Airport;
import pojo.Flight;
import pojo.FlightPlace;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class StubUtils {

    public static List<Airport> getAirports() {
        AirportService airportService = new AirportService();
        List<Airport> airports = airportService.getAll();
        return airports;
    }

    public static List<Airplane> getAirplanes() {
        AirplaneService airplaneService = new AirplaneService();
        List<Airplane> airplanes = airplaneService.getAll();
        return airplanes;
    }

    public static List<Flight> getFlights(List<Airport> airports, List<Airplane> airplanes) {
        //TODO: этот кусок вынести в DAOimpl можно!

        Connection connection = DataSource.getConnection();
        List<Flight> flights = new ArrayList<>();
        try {
            String sql = "SELECT * FROM flight";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Long id = result.getLong("id");
                Long airplaneId = result.getLong("airplane_id");
                String flightNumber = result.getString("flight_number");
                Long departureAirportId = result.getLong("departure_airport_id");
                Long arrivalAirportId = result.getLong("arrival_airport_id");
                Integer availableEconom = result.getInt("available_places_econom");
                Integer availableBusiness = result.getInt("available_places_business");
                Airport departureAirport = new Airport();
                Airport arrivalAirport = new Airport();
                for (Airport airport : airports) {
                    if (departureAirportId == airport.getAirportId())
                        departureAirport = airport;
                    if (arrivalAirportId == airport.getAirportId())
                        arrivalAirport = airport;
                }
                Airplane airplaneFlight = new Airplane();
                for (Airplane airplane : airplanes) {
                    if (airplaneId.equals(airplane.getAirplaneId())) {
                        airplaneFlight = airplane;
                    }
                }
                Double baseCost = result.getDouble("base_cost");
                LocalDateTime dateTime = result.getTimestamp("flight_datetime").toLocalDateTime();
                flights.add(new Flight(id, airplaneFlight, flightNumber, departureAirport, arrivalAirport, baseCost, availableEconom, availableBusiness, dateTime));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flights;
    }

    public static int randomSittingPlace(long flightId, boolean business) {
        int reservedPlace = 0;
        FlightPlace flightPlace = null;
        FlightPlaceService flightPlaceService = new FlightPlaceService();
        Optional<FlightPlace> flightPlaceOptional = flightPlaceService.getByFlight((int) flightId);

        Flight flight = null;
        FlightService flightService = new FlightService();
        Optional<Flight> flightOptional = flightService.get((int)flightId);
        if(flightOptional.isPresent()){
            flight = flightOptional.get();
        }

        if (flightPlaceOptional.isPresent()) {
            flightPlace = flightPlaceOptional.get();
            BitSet places;
            if (business) {
                places = flightPlace.getBitPlacesBusiness();
            }
            else {
                places = flightPlace.getBitPlacesEconom();
            }
            System.out.println(places);
            System.out.println(places.length());
            int length = places.length();
            Random random = new Random(47);
            for (int i = 0; i < places.length(); i++) {
                reservedPlace = random.nextInt(length);
                if (places.get(reservedPlace)) {
                    reservedPlace = random.nextInt(length);
                } else {
                    places.set(reservedPlace);

                    if (business) {
                        flight.setAvailablePlacesBusiness(flight.getAvailablePlacesEconom() - 1);
                        flightPlace.setBitPlacesBusiness(places);
                    }
                    else {
                        flight.setAvailablePlacesEconom(flight.getAvailablePlacesEconom()-1);
                        flightPlace.setBitPlacesEconom(places);
                    }

                    System.out.println("reservedPlace:" + reservedPlace);
                    System.out.println("places reserved:" + places);
                    flightPlaceService.update(flightPlace);
                    flightService.update(flight);
                    break;
                }
            }
        }
        return reservedPlace;
    }

    public static BitSet bitSetConversionFromString(String string) {
        BitSet bitSet = new BitSet(string.length());
        bitSet.set(0, bitSet.length(), false);
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == '1') {
                bitSet.set(i);
            }
        }
        return bitSet;
    }

    public static String stringConversionToBitSet(BitSet bitSet) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < bitSet.size(); i++) {
            if (bitSet.get(i)) {
                stringBuilder.append('1');
            } else stringBuilder.append('0');
        }
        return stringBuilder.toString();
    }
}