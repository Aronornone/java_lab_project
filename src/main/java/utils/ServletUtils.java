package utils;

import db.DataSource;
import db.service.*;
import pojo.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

public class ServletUtils {
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

    /**
     * Method returns and reserved random place in airplane after user has add it to cart.
     * Set bit for place with BitSet in FlightPlace and decrease available places of ticket class in Flight.
     *
     * @param flightId id of Flight to reserve.
     * @param business if true it will get random place from BitSet in business class and reserved it.
     * @return random reserved place for this flight in int
     */
    public static int getRandomSittingPlace(long flightId, boolean business) {
        FlightPlace flightPlace = fps.getByFlightId((int) flightId).get();
        Flight flight = fs.get((int) flightId).get();

        OurBitSet placesBitSet;
        int availablePlacesInClass;

        if (business) {
            placesBitSet = flightPlace.getBitPlacesBusiness();
            availablePlacesInClass = flight.getAvailablePlacesBusiness();
        } else {
            placesBitSet = flightPlace.getBitPlacesEconom();
            availablePlacesInClass = flight.getAvailablePlacesEconom();
        }

        int lengthPlacesBitSet = placesBitSet.length();
        int reservedPlace = 0;
        while (true) {
            if (availablePlacesInClass == 0) {
                //TODO: logging
                System.out.println("Not enough placesBitSet");
                break;
            }
            reservedPlace = RandomGenerator.createNumber(1, lengthPlacesBitSet);
            if (placesBitSet.get(reservedPlace)) {
                continue;
            }
            placesBitSet.set(reservedPlace);
            if (business) {
                flight.setAvailablePlacesBusiness(flight.getAvailablePlacesBusiness() - 1);
            } else {
                flight.setAvailablePlacesEconom(flight.getAvailablePlacesEconom() - 1);
            }
            fs.update(flight);
            flight = fs.get((int) flightId).get();
            flightPlace = fps.getByFlightId((int) flight.getFlightId()).get();
            if (business) {
                flightPlace.setBitPlacesBusiness(placesBitSet);
            } else {
                flightPlace.setBitPlacesEconom(placesBitSet);
            }
            fps.update(flightPlace);
            break;
        }
        return reservedPlace;
    }

    /**
     * Method for getting number of tickets in created invoice of user (exactly bucket)
     *
     * @param user user for whom we check number of tickets in invoice to view in bucket
     * @return
     */
    public static int getNumberOfTicketsInInvoice(User user) {
        Invoice invoice;
        int numberOfTicketsInInvoice = 0;

        if (is.getInvoiceByUser(user.getUserId(), Invoice.InvoiceStatus.CREATED).isPresent()) {
            invoice = is.getInvoiceByUser(user.getUserId(), Invoice.InvoiceStatus.CREATED).get();
            List<Ticket> tickets = ts.getTicketsByInvoice(invoice.getInvoiceId());
            numberOfTicketsInInvoice = tickets.size();
        }
        return numberOfTicketsInInvoice;
    }

    /**
     * Method for revert places to flights if session of user is ended and he doesn't pay for invoice.
     * If you use it for reset places in ticket from econom to business and conversely,
     * you must first revert places, then change ticket class (to business\econom).
     * After reverting tickets are deleted.
     *
     * @param tickets List of tickets on which we must update sitting places to flight and bitsetFlightPlace
     */
    public static void revertSittingPlaces(List<Ticket> tickets) {
        Flight flight;
        for (Ticket ticket : tickets) {
            long flightId = ticket.getFlight().getFlightId();
            FlightPlace flightPlace = fps.getByFlightId((int) flightId).get();
            flight = fs.get((int) flightId).get();
            OurBitSet newPlacesBitSet;
            if (ticket.isBusinessClass()) {
                flight.setAvailablePlacesBusiness(flight.getAvailablePlacesBusiness() + 1);
                newPlacesBitSet = flightPlace.getBitPlacesBusiness();
                newPlacesBitSet.clear(ticket.getSittingPlace());
                flightPlace.setBitPlacesBusiness(newPlacesBitSet);
            } else {
                flight.setAvailablePlacesEconom(flight.getAvailablePlacesEconom() + 1);
                newPlacesBitSet = flightPlace.getBitPlacesEconom();
                newPlacesBitSet.clear(ticket.getSittingPlace());
                flightPlace.setBitPlacesEconom(newPlacesBitSet);
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
    public static String stringConversionFromBitSet(BitSet bitSet) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < bitSet.length(); i++) {
            if (bitSet.get(i)) {
                stringBuilder.append('1');
            } else stringBuilder.append('0');
        }
        return stringBuilder.toString();
    }

    /**
     * Method for check if all fields of tickets in invoice are contains information. Then it saves ticket info, anyway.
     *
     * @param ticketsIds     array of Strings tickets ids
     * @param passengerNames array of Strings passenger names
     * @param passports      array of Strings passports
     * @return true if some of fields are empty, false if all fields are fill right.
     */
    public static boolean isEmptyWhilePayAndSave(String[] ticketsIds, String[] passengerNames,
                                                 String[] passports, String[] luggages) {
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

            List<String> luggagesList;
            boolean[] luggagesBoolean = new boolean[ticketsList.size()];

            if (luggages != null) {
                luggagesList = Arrays.asList(luggages);
                for (int i = 0; i < luggagesList.size(); i++) {
                    luggagesBoolean[i] = (luggagesList.get(i).equals("luggage"));
                }
            }
            updateTicketWhilePay(ticketsIds, passengerNames, passports, luggagesBoolean);
        } else empty = true;
        return empty;
    }

    /**
     * Method for update information of ticket based on what client set in fields
     *
     * @param ticketsIds     array of Strings tickets ids
     * @param passengerNames array of Strings passenger names
     * @param passports      array of Strings passports
     */
    private static void updateTicketWhilePay(String[] ticketsIds, String[] passengerNames,
                                             String[] passports, boolean[] luggages) {
        for (int i = 0; i < ticketsIds.length; i++) {
            Ticket ticketToUpdate = ts.get(Long.parseLong(ticketsIds[i])).get();
            ticketToUpdate.setPassengerName(passengerNames[i]);
            ticketToUpdate.setPassport(passports[i]);
            ticketToUpdate.setLuggage(luggages[i]);
            ts.update(ticketToUpdate);
        }
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

    public static int getAmountFlights(long arrival, long departure, String dateFrom, String dateTo) {
        Connection con = DataSource.getConnection();
        int i = 0;
        try {
            String sql = "SELECT COUNT(*) AS tt FROM flight " +
                    "WHERE " +
                    "arrival_airport_id="+arrival+" "+
                    "AND " +
                    "departure_airport_id="+departure+" "+
                    "AND " +
                    "flight_datetime>'" + dateFrom + "' " +
                    "AND " +
                    "flight_datetime<'" + dateTo + "'";
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

        System.out.println("Found results (how many): "+getAmountFlights(arrival, departure, dateFrom, dateTo));
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