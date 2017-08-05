package stubs;

import db.DataSource;
import db.service.*;
import pojo.*;
import utils.FlightHelper;
import utils.OurBitSet;
import utils.RandomGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StubUtils {
    private static FlightPlaceService fps = new FlightPlaceService();
    private static FlightService fs = new FlightService();
    private static AirportService as = new AirportService();
    private static AirplaneService aps = new AirplaneService();
    private static TicketService ts = new TicketService();
    private static InvoiceService is = new InvoiceService();

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
    public static int getRandomSittingPlace(long flightId, boolean business) {
        FlightPlace flightPlace = fps.getByFlightId((int) flightId).get();
        Flight flight = fs.get((int) flightId).get();

        OurBitSet places;
        int available;

        if (business) {
            places = flightPlace.getBitPlacesBusiness();
            available = flight.getAvailablePlacesBusiness();
        } else {
            places = flightPlace.getBitPlacesEconom();
            available = flight.getAvailablePlacesEconom();
        }

        int length = places.length();
        int reservedPlace = 0;
        while (true) {
            if (available == 0) {
                System.out.println("Not enough places");
                break;
            }

            reservedPlace = RandomGenerator.createNumber(1, length);
            if (places.get(reservedPlace)) {
                continue;
            }
            places.set(reservedPlace);
            if (business) {
                flight.setAvailablePlacesBusiness(flight.getAvailablePlacesBusiness() - 1);
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
        return reservedPlace;
    }

    /**
     * Method for getting number of tickets in created invoice of user
     *
     * @param user user for whom we check number of tickets in invoice to view in bucket
     * @return
     */
    public static int getNumberOfTicketsInInvoice(User user) {
        Invoice invoice;
        int numberOfTickets = 0;

        if (is.getInvoiceByUser(user.getUserId(), Invoice.InvoiceStatus.CREATED).isPresent()) {
            invoice = is.getInvoiceByUser(user.getUserId(), Invoice.InvoiceStatus.CREATED).get();
            List<Ticket> tickets = ts.getTicketsByInvoice(invoice.getInvoiceId());
            numberOfTickets = tickets.size();
        }
        return numberOfTickets;
    }

    /**
     * Method to revert places to flights.
     * If you use it for reset places in ticket from econom to business and conversely,
     * you must first revert places, then chenge ticket class (to business\econom)
     *
     * @param tickets List of tickets on which we must return sitting places to flight and bitsetFlightPlace
     */
    public static void revertSittingPlaces(List<Ticket> tickets) {
        Flight flight;
        for (Ticket ticket : tickets) {
            long flightId = ticket.getFlight().getFlightId();
            FlightPlace flightPlace = fps.getByFlightId((int) flightId).get();
            flight = fs.get((int) flightId).get();
            OurBitSet newBitSet;
            if (ticket.isBusinessClass()) {
                flight.setAvailablePlacesBusiness(flight.getAvailablePlacesBusiness() + 1);
                newBitSet = flightPlace.getBitPlacesBusiness();
                newBitSet.clear(ticket.getSittingPlace());
                flightPlace.setBitPlacesBusiness(newBitSet);
            } else {
                flight.setAvailablePlacesEconom(flight.getAvailablePlacesEconom() + 1);
                newBitSet = flightPlace.getBitPlacesEconom();
                newBitSet.clear(ticket.getSittingPlace());
                flightPlace.setBitPlacesEconom(newBitSet);
            }
            fps.update(flightPlace);
            fs.update(flight);
            ts.remove(ticket);
        }
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

    public static boolean checkEmptyAndSaveForPay(String[] ticketsIds, String[] passengerNames, String[] passports) {
        boolean empty = false;
        if (ticketsIds != null && passengerNames != null && passports != null) {
            List<String> ticketsList = Arrays.asList(ticketsIds);
            List<String> passengersList = Arrays.asList(passengerNames);
            List<String> passportsList = Arrays.asList(passports);

            for (String string : ticketsList) {
                if (string.isEmpty()) empty = true;
            }
            for (String string : passengersList) {
                if (string.isEmpty()) empty = true;
            }
            for (String string : passportsList) {
                if (string.isEmpty()) empty = true;
            }
            for (int i = 0; i < ticketsIds.length; i++) {
                Ticket ticketToUpdate = ts.get(Long.parseLong(ticketsIds[i])).get();
                ticketToUpdate.setPassengerName(passengerNames[i]);
                ticketToUpdate.setPassport(passports[i]);
                ts.update(ticketToUpdate);
            }
        } else empty = true;
        return empty;
    }

    public static String generateButtons(int i) {
        int FLIGHTS_PER_PAGE = 2;
        int pages = 2;
        StringBuilder sb = new StringBuilder();
        while (i - 2 >= 0) {
            sb.append(generateOneButton(pages++)).append(" ");
            i -= 2;
        }
        return sb.toString();
    }

    public static String generateOneButton(int number) {
        StringBuilder sb = new StringBuilder();
        sb.append("<a href=\"/\"").append(number).append("\\\">").append(number).append("</a>");
        return sb.toString();
    }

    public static int getAmountFlights(String arrival, String departure) {
        Connection con = DataSource.getConnection();
        int i = 0;
        try {
            String sql = "SELECT COUNT(*) AS tt FROM flight " +
                    "WHERE " +
                    "arrival_airport_id=(SELECT id FROM airport WHERE airport_name='" + arrival + "') " +
                    "AND " +
                    "departure_airport_id=(SELECT id FROM airport WHERE airport_name='" + departure + "')";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            rs.next();
            i = rs.getInt("tt");
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * @param departure     id departured airport
     * @param arrival       id arrival airport
     * @param dateFrom      date to departure
     * @param dateTo        date till departure need to be done
     * @param requiredSeats
     * @param business
     * @param numberOfPage
     * @return
     */
    public static List<FlightHelper> getFlights(long departure, long arrival, String dateFrom, String dateTo,
                                                int requiredSeats, boolean business, int numberOfPage) {
        Connection con = DataSource.getConnection();
        int FLIGHTS_PER_PAGE = 10;
        List<FlightHelper> flights = new ArrayList<>();
        try {
            String checkSeats = "";
            if (business) {
                checkSeats = " AND available_places_business>=" + requiredSeats + " ";
            } else {
                checkSeats = " AND available_places_econom>=" + requiredSeats + " ";
            }
            String sql = "SELECT  * FROM   (SELECT * FROM Flight WHERE flight_datetime>'" + dateFrom +
                    "'  AND flight_datetime<'" + dateTo + "' AND departure_airport_id=" +
                    departure + " AND arrival_airport_id=" + arrival + checkSeats + ") " +
                    "AS tt ORDER BY flight_datetime LIMIT "
                    + (numberOfPage - 1) * FLIGHTS_PER_PAGE + "," +
                    numberOfPage * FLIGHTS_PER_PAGE;
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();

            while (result.next()) {
                flights.add(new FlightHelper(result.getLong("id"),
                        result.getDouble("base_cost"), result.getString("flight_number"), departure, arrival, result.getTimestamp("flight_datetime").toLocalDateTime()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flights;
    }


}