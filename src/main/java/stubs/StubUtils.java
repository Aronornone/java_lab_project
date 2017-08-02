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
import utils.OurBitSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StubUtils {
    private static FlightPlaceService fps = new FlightPlaceService();
    private static FlightService fs = new FlightService();
    private static AirportService as = new AirportService();
    private static AirplaneService aps = new AirplaneService();

    public static List<Airport> getAirports() {
        List<Airport> airports = as.getAll();
        return airports;
    }

    public static List<Airplane> getAirplanes() {
        List<Airplane> airplanes = aps.getAll();
        return airplanes;
    }

    public static List<Flight> getFlights(List<Airport> airports, List<Airplane> airplanes) {

        //TODO: change for DAO
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

    /**
     * Method returns and reserved random place in airplane.
     * Set bit for place with BitSet in FlightPlace and decrease available places of ticket class in Flight.
     *
     * @param flightId id of Flight to reserve.
     * @param business if true it will get random place from BitSet in business class and reserved it.
     * @return random reserved place for this flight in int
     */
    public static int randomSittingPlace(long flightId, boolean business) {
        int reservedPlace = 0;
        FlightPlace flightPlace = fps.getByFlightId((int) flightId).get();
        Flight flight = fs.get((int) flightId).get();

        OurBitSet places;

        if (business) {
            places = flightPlace.getBitPlacesBusiness();
        } else {
            places = flightPlace.getBitPlacesEconom();
        }
        int length = places.length();

        Random random = new Random(47);
        for (int i = 1; i < length; i++) {
            reservedPlace = random.nextInt(length);
            if (places.get(reservedPlace)) {
                reservedPlace = random.nextInt(length);
            } else {
                places.set(reservedPlace);
                if (business) {
                    flight.setAvailablePlacesBusiness(flight.getAvailablePlacesEconom() - 1);
                } else {
                    flight.setAvailablePlacesEconom(flight.getAvailablePlacesEconom() - 1);
                }
                fs.update(flight);
                flight = fs.get((int) flightId).get();
                flightPlace = fps.getByFlightId((int) flight.getFlightId()).get();
                if (business) {
                    flightPlace.setBitPlacesBusiness(places);
                } else {
                    flightPlace.setBitPlacesEconom(places);
                }
                fps.update(flightPlace);
                break;
            }
        }
        return reservedPlace;
    }

    /**
     * Util method for ease work with BitSet from Mysql. Convert String to OurBitSet object.
     *
     * @param string String with sequence of 1 and 0 bits
     * @return OurBitSet that contains installed bits from String
     */
    public static OurBitSet bitSetConversionFromString(String string) {
        OurBitSet bitSet = new OurBitSet(string.length());
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == '1') {
                bitSet.set(i);
            }
        }
        return bitSet;
    }

    /**
     * Util method for ease work with BitSet from Mysql. Convert OurBitSet object to String.
     *
     * @param bitSet OurBitSet that contains installed bits
     * @return String with sequence of 1 and 0 bits
     */
    public static String stringConversionFromBitSet(OurBitSet bitSet) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < bitSet.length(); i++) {
            if (bitSet.get(i)) {
                stringBuilder.append('1');
            } else stringBuilder.append('0');
        }
        return stringBuilder.toString();
    }
}