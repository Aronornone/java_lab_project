package dao.daoimpl;

import dao.DataSource;
import dao.interfaces.FlightDAO;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import pojo.Airplane;
import pojo.Airport;
import pojo.Flight;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class FlightDAOImpl implements FlightDAO {
    private static final String SELECT_ALL =
            "SELECT\n" +
            "  f.id, airplane_id, p.name, p.capacity_econom, p.capacity_business, flight_number, " +
            "  departure_airport_id, d.code, d.city, d.airport_name, d.latitude, d.longitude, " +
                    "arrival_airport_id, a.code, a.city, a.airport_name, a.latitude, a.longitude, " +
            "  base_cost, available_places_econom, available_places_business, flight_datetime " +
            "FROM Flight f\n" +
            "  JOIN Airplane p ON p.id = f.airplane_id\n" +
            "  JOIN Airport  d ON d.id = f.departure_airport_id\n" +
            "  JOIN Airport  a ON a.id = f.arrival_airport_id\n";
    private static final String ORDER_BY_DATETIME_AND_BASECOST = "ORDER BY flight_datetime, base_cost";
    private static Logger log = Logger.getLogger("DBLogger");

    private final static FlightDAO instance = new FlightDAOImpl();

    public static FlightDAO getInstance() {
        log.info("getInstance(): Returning instance of FlightDAOImpl.");
        return instance;
    }

    private FlightDAOImpl() {
    }

    @Override
    @SneakyThrows
    public void add(Flight flight) {
        log.info("add(flight): Received the following 'flight': " + flight);
        String sql = "INSERT INTO Flight (airplane_id, flight_number, departure_airport_id, arrival_airport_id, " +
                "base_cost, available_places_econom, available_places_business, flight_datetime) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        log.info("add(flight): Trying to create connection to data source and prepare a query.");
        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("add(flight): Putting parameters of specified types into their PreparedStatement positions.");
            ps.setLong     (1, flight.getAirplane().getAirplaneId());
            ps.setString   (2, flight.getFlightNumber());
            ps.setLong     (3, flight.getDepartureAirport().getAirportId());
            ps.setLong     (4, flight.getArrivalAirport().getAirportId());
            ps.setDouble   (5, flight.getBaseCost());
            ps.setInt      (6, flight.getAvailablePlacesEconom());
            ps.setInt      (7, flight.getAvailablePlacesBusiness());
            ps.setTimestamp(8, Timestamp.valueOf(flight.getDateTime()));

            log.info("add(flight): Executing the query: " + ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("add(flight): SQL exception code: " + e.getErrorCode());
        }
    }

    @Override
    @SneakyThrows
    public Optional<Flight> get(long id) {
        log.info("get(id): Received the following 'id': " + id);
        String sql = SELECT_ALL + "WHERE f.id = ?\n" + ORDER_BY_DATETIME_AND_BASECOST;

        log.info("get(id): Creating a null 'flight' object");
        Flight flight = null;
        log.info("get(id): Trying to create a connection to data source and prepare a query.");
        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("get(id): Putting 'id' = " + id + " into its PreparedStatement position.");
            ps.setLong(1, id);

            log.info("get(id): Trying to execute the query and put result to ResultSet: " + ps);
            try(ResultSet rs = ps.executeQuery()) {
                log.info("get(id): Creating a 'flight' object from ResultSet.");
                while (rs.next()) {
                    flight = createNewFlight(rs);
                }
            }
        } catch (SQLException e) {
            log.error("get(id): SQL exception code: " + e.getErrorCode());
        }

        log.info("get(id): Returning a 'flight' object: " + flight);
        return Optional.ofNullable(flight);
    }

    @Override
    @SneakyThrows
    public void update(Flight flight) {
        log.info("update(airport): Received the following 'flight': " + flight);
        String sql = "UPDATE Flight SET airplane_id = ?, flight_number = ?, departure_airport_id = ?, arrival_airport_id =?, " +
                "base_cost = ?, available_places_econom = ?, available_places_business = ?, flight_datetime = ? WHERE id = ?";

        log.info("update(flight): Trying to create a connection to a data source and prepare a query.");
        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("update(flight): Putting parameters of specified types into their PreparedStatement positions.");
            ps.setLong     (1, flight.getAirplane().getAirplaneId());
            ps.setString   (2, flight.getFlightNumber());
            ps.setLong     (3, flight.getDepartureAirport().getAirportId());
            ps.setLong     (4, flight.getArrivalAirport().getAirportId());
            ps.setDouble   (5, flight.getBaseCost());
            ps.setInt      (6, flight.getAvailablePlacesEconom());
            ps.setInt      (7, flight.getAvailablePlacesBusiness());
            ps.setTimestamp(8, Timestamp.valueOf(flight.getDateTime()));
            ps.setLong     (9, flight.getFlightId());

            log.info("update(flight): Executing the query: " + ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("update(airport): SQL exception code: " + e.getErrorCode());
        }
    }

    @Override
    @SneakyThrows
    public void delete(Flight flight) {
        log.info("delete(flight): Received the following 'flight': " + flight);
        String sql = "DELETE FROM Flight WHERE id = ?";

        log.info("delete(flight): Trying to create a connection to data source and prepare a query.");
        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("delete(flight): Putting 'flight_id' = " + flight.getFlightId() + " into its PreparedStatement position.");
            ps.setLong(1, flight.getFlightId());

            log.info("delete(flight): Executing the query: " + ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("delete(flight): SQL exception code: " + e.getErrorCode());
        }
    }

    @Override
    @SneakyThrows
    public List<Flight> getAll() {
        log.info("getAll(): Creating an empty list of flights.");
        List<Flight> flights = new ArrayList<>();
        log.info("getAll(): Trying to create a connection to a data source, prepare a query, execute it and put result into ResultSet.");
        try(Connection connection = DataSource.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(SELECT_ALL + ORDER_BY_DATETIME_AND_BASECOST)) {
            log.info("getAll(): Adding flights from ResultSet to the list.");
            while (rs.next()) {
                flights.add(createNewFlight(rs));
            }
        } catch (SQLException e) {
            log.error("getAll(): SQL exception code: " + e.getErrorCode());
        }

        log.info("getAll(): Returning the list of flights.");
        return flights;
    }

    @Override
    @SneakyThrows
    public List<Flight> getFlights(long departure, long arrival, String dateFrom, String dateTo, int requiredSeats, boolean business, int numberOfPage) {
        log.info("getFlights(...): Initializing 'FLIGHTS_PER_PAGE' variable.");
        int FLIGHTS_PER_PAGE = 10;
        log.info("getFlights(...): Creating an empty list of flights.");

        log.info("getFlights(...): Declaring 'checkSeats' string variable.");
        String checkSeats;
        log.info("getFlights(...): Checking if 'business' = true.");
        if (business) {
            log.info("getFlights(...): Creating a part of sql query for available business places.");
            checkSeats = " AND available_places_business>=" + requiredSeats + " ";
        } else {
            log.info("getFlights(...): Creating a part of sql query for available econom places.");
            checkSeats = " AND available_places_econom>=" + requiredSeats + " ";
        }
        log.info("getFlights(...): Initializing an 'sql' variable.");
        String sql = "SELECT * FROM (SELECT * FROM Flight " +
                "WHERE flight_datetime >= '" + dateFrom + "' AND flight_datetime <= '" + dateTo + "' " +
                "AND departure_airport_id = " + departure + " AND arrival_airport_id = " + arrival + checkSeats + ") " +
                "AS tt ORDER BY flight_datetime LIMIT " + (numberOfPage) * FLIGHTS_PER_PAGE + "," + FLIGHTS_PER_PAGE;

        log.info("getFlights(...): Creating an empty list of flights.");
        List<Flight> flights = new ArrayList<>();
        log.info("getFlights(...): Trying to create a connection to a data source, prepare a query.");
        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("getFlights(...): Trying to execute the query and put result to ResultSet: " + ps);
            try(ResultSet rs = ps.executeQuery()) {
                log.info("getFlights(...): Adding flights from ResultSet to the list.");
                while (rs.next()) {
                    flights.add(new Flight(
                            rs.getLong     ("id"),
                            rs.getString   ("flight_number"),
                            rs.getDouble   ("base_cost"),
                            rs.getInt       ("available_places_econom"),
                            rs.getInt       ("available_places_business"),
                            rs.getTimestamp("flight_datetime").toLocalDateTime(),
                            departure,
                            arrival
                    ));
                }
            }
        } catch (SQLException e) {
            log.error("getFlights(...): SQL exception code: " + e.getErrorCode());
        }

        log.info("getFlights(...): Returning the list of flights.");
        return flights;
    }

    @Override
    @SneakyThrows
    public int getAmountFlights(long arrival, long departure, String dateFrom, String dateTo, int requiredSeats, boolean business) {
        log.info("getAmountFlights(...): Initializing 'i' variable.");
        int i = 0;

        log.info("getAmountFlights(...): Declaring 'checkSeats' string variable.");
        String checkSeats;
        log.info("getAmountFlights(...): Checking if 'business' = true.");
        if (business) {
            log.info("getAmountFlights(...): Creating a part of sql query for available business places.");
            checkSeats = " AND available_places_business>=" + requiredSeats + " ";
        } else {
            log.info("getAmountFlights(...): Creating a part of sql query for available econom places.");
            checkSeats = " AND available_places_econom>=" + requiredSeats + " ";
        }
        log.info("getAmountFlights(...): Initializing an 'sql' variable.");
        String sql = "SELECT COUNT(*) AS tt FROM flight " +
                "WHERE arrival_airport_id=" + arrival + " " +
                "AND departure_airport_id=" + departure + " " +
                "AND flight_datetime>'" + dateFrom + "' " +
                "AND flight_datetime<'" + dateTo + "' " +
                checkSeats + ";";

        log.info("getAmountFlights(...): Trying to create a connection to a data source, prepare a query.");
        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){
            log.info("getAmountFlights(...): Trying to execute the query and put result to ResultSet: " + ps);
            try(ResultSet rs = ps.executeQuery()) {
                log.info("getAmountFlights(...): Executing rs.next()");
                rs.next();
                log.info("getAmountFlights(...): Initializing 'i' variable with rs.getInt(\"tt\")");
                i = rs.getInt("tt");
            }
        } catch (SQLException e) {
            log.error("getAmountFlights(...): SQL exception code: " + e.getErrorCode());
        }

        log.info("getAmountFlights(...): Returning amount of flights.");
        return i;
    }

    @SneakyThrows
    private Flight createNewFlight(ResultSet rs) {
        return new Flight(
                rs.getLong("id"),
                new Airplane(
                        rs.getLong  ("airplane_id"),
                        rs.getString("p.name"),
                        rs.getInt   ("p.capacity_econom"),
                        rs.getInt   ("p.capacity_business")
                ),
                rs.getString("flight_number"),
                new Airport(
                        rs.getLong  ("departure_airport_id"),
                        rs.getString("d.code"),
                        rs.getString("d.city"),
                        rs.getString("d.airport_name"),
                        rs.getDouble("d.latitude"),
                        rs.getDouble("d.longitude")
                ),
                new Airport(
                        rs.getLong  ("arrival_airport_id"),
                        rs.getString("a.code"),
                        rs.getString("a.city"),
                        rs.getString("a.airport_name"),
                        rs.getDouble("a.latitude"),
                        rs.getDouble("a.longitude")
                ),
                rs.getDouble   ("base_cost"),
                rs.getInt      ("available_places_econom"),
                rs.getInt      ("available_places_business"),
                rs.getTimestamp("flight_datetime").toLocalDateTime()
        );
    }
}
