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
import utils.FlightHelper;

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
    public static String generateButtons(int i) {
        int FLIGHTS_PER_PAGE=2;
        int pages=2;
        StringBuilder sb=new StringBuilder();
        while (i-2>=0){
            sb.append(generateOneButton(pages++)).append(" ");
            i-=2;
        }
        return sb.toString();

    }

    public static String generateOneButton(int number){
        StringBuilder sb=new StringBuilder();
        sb.append("<a href=\"/\"").append(number).append("\\\">").append(number).append("</a>");
        return sb.toString();

    }
    public static int getAmountFlights(String arrival, String departure){
        Connection con=DataSource.getConnection();
        int i=0;
        try{
            String sql= "SELECT COUNT(*) AS tt FROM flight " +
                    "WHERE " +
                    "arrival_airport_id=(SELECT id FROM airport WHERE airport_name='"+arrival+"') " +
                    "AND " +
                    "departure_airport_id=(SELECT id FROM airport WHERE airport_name='"+departure+"')";
            PreparedStatement ps=con.prepareStatement(sql);
            ResultSet rs =ps.executeQuery();
            rs.next();
            i=rs.getInt("tt");
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }


    /**
     *
     * @param departure id departured airport
     * @param arrival id arrival airport
     * @param dateFrom date to departure
     * @param dateTo date till departure need to be done
     * @param requiredSeats
     * @param business
     * @param numberOfPage
     * @return
     */
    public static List<FlightHelper> getFlights(long departure, long arrival, String dateFrom, String dateTo, int requiredSeats, boolean business, int numberOfPage){
        Connection con = DataSource.getConnection();
        int FLIGHTS_PER_PAGE=2;
        List<FlightHelper> flights = new ArrayList<>();
        try{
            String checkSeats="";
            if (business){
                checkSeats=" AND available_places_business>="+requiredSeats+" ";
            } else {
                if (business){
                    checkSeats=" AND available_places_econom>="+requiredSeats+" ";
                }
            }
            String sql="SELECT  * FROM   (SELECT * FROM Flight WHERE flight_datetime>'"+dateFrom+"'  AND flight_datetime<'"+dateTo+"' AND departure_airport_id="+departure+" AND arrival_airport_id="+arrival+checkSeats+") AS tt ORDER BY flight_datetime LIMIT "
                    +(numberOfPage-1)*FLIGHTS_PER_PAGE+","+numberOfPage*FLIGHTS_PER_PAGE;
            PreparedStatement ps=con.prepareStatement(sql);
            ResultSet result =ps.executeQuery();


            while (result.next()) {
                flights.add(new FlightHelper(result.getLong("id"),
                        result.getDouble("base_cost"), result.getString("flight_number"),departure, arrival,  result.getTimestamp("flight_datetime").toLocalDateTime()));

            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return flights;
    }
}