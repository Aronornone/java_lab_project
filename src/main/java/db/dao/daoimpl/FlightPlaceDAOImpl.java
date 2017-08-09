package db.dao.daoimpl;

import db.dao.DataSource;
import db.dao.interfaces.FlightPlaceDAO;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import pojo.Airplane;
import pojo.Airport;
import pojo.Flight;
import pojo.FlightPlace;
import utils.ServletUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class FlightPlaceDAOImpl implements FlightPlaceDAO {
    private static final String SELECT_ALL =
            "SELECT\n" +
            " fp.id, fp.flight_id, f.airplane_id, p.name, p.capacity_econom, p.capacity_business, f.flight_number,\n" +
            " f.departure_airport_id, d.code, d.city, d.airport_name,d.latitude,d.longitude, " +
            " f.arrival_airport_id, a.code, a.city, a.airport_name, a.latitude, a.longitude, \n" +
            " f.base_cost, f.available_places_econom, f.available_places_business, f.flight_datetime,\n" +
            " places_econom, places_business\n " +
            "FROM FlightPlace fp\n" +
            " JOIN Flight   f ON f.id = fp.flight_id\n" +
            " JOIN Airplane p ON p.id = f.airplane_id\n" +
            " JOIN Airport  d ON d.id = f.departure_airport_id\n" +
            " JOIN Airport  a ON a.id = f.arrival_airport_id\n";
    private static final String ORDER_BY_DATETIME = " ORDER BY f.flight_datetime ";
    private static Logger log = Logger.getLogger("DBLog");

    private final static FlightPlaceDAO instance = new FlightPlaceDAOImpl();

    public static FlightPlaceDAO getInstance() {
        log.info("getInstance(): Returning instance of FlightPlaceDAOImpl.");
        return instance;
    }

    private FlightPlaceDAOImpl() {
    }

    @Override
    @SneakyThrows
    public void add(FlightPlace flightPlaces) {
        log.info("add(flightPlaces): Received the following 'flightPlaces': " + flightPlaces);
        String sql = "INSERT INTO FlightPlace (flight_id, places_econom, places_business) " +
                "VALUES (?, ?, ?)";

        log.info("add(flightPlaces): Trying to create a connection to a data source and prepare a query.");
        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("add(flightPlaces): Putting parameters of specified types into their PreparedStatement positions.");
            ps.setLong  (1, flightPlaces.getFlight().getFlightId());
            ps.setString(2, String.valueOf(flightPlaces.getBitPlacesEconom()));
            ps.setString(3, String.valueOf(flightPlaces.getBitPlacesBusiness()));

            log.info("add(flightPlaces): Executing the query: " + ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("add(flightPlaces): SQL exception code: " + e.getErrorCode());
        }
    }

    @Override
    @SneakyThrows
    public Optional<FlightPlace> get(long id) {
        log.info("get(id): Received the following 'id': " + id);
        String sql = SELECT_ALL + "WHERE fp.id = ?\n" + ORDER_BY_DATETIME;

        log.info("get(id): Creating a null 'flightPlaces' object");
        FlightPlace flightPlaces = null;
        log.info("get(id): Trying to create a connection to a data source and prepare a query.");
        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("get(id): Putting 'id' = " + id + " into its PreparedStatement position.");
            ps.setLong(1, id);

            log.info("get(id): Trying to execute the query and put result to ResultSet: " + ps);
            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    log.info("get(id): Creating a 'flightPlaces' object from ResultSet.");
                    flightPlaces = createNewFlightPlace(rs);
                }
            }
        } catch (SQLException e) {
            log.error("get(id): SQL exception code: " + e.getErrorCode());
        }

        log.info("get(id): Returning an 'flightPlaces' object: " + flightPlaces);
        return Optional.ofNullable(flightPlaces);
    }

    @Override
    @SneakyThrows
    public Optional<FlightPlace> getByFlightId(long flightId) {
        log.info("getByFlightId(flightId): Received the following 'flightId': " + flightId);
        String sql = SELECT_ALL + " WHERE fp.flight_id = ? " + ORDER_BY_DATETIME;

        log.info("getByFlightId(flightId): Creating a null 'flightPlaces' object");
        FlightPlace flightPlaces = null;
        log.info("getByFlightId(flightId): Trying to create a connection to data source and prepare a query.");
        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("getByFlightId(flightId): Putting 'flightId' = " + flightId + " into its PreparedStatement position.");
            ps.setLong(1, flightId);

            log.info("getByFlightId(flightId): Trying to execute the query and put result to ResultSet: " + ps);
            try(ResultSet rs = ps.executeQuery()) {
                log.info("getByFlightId(flightId): Creating a 'flightPlaces' object from ResultSet.");
                while (rs.next()) {
                    flightPlaces = createNewFlightPlace(rs);
                }
            }
        } catch (SQLException e) {
            log.error("getByFlightId(flightId): SQL exception code: " + e.getErrorCode());
        }

        log.info("getByFlightId(flightId): Returning an 'flightPlaces' object: " + flightPlaces);
        return Optional.ofNullable(flightPlaces);
    }

    @Override
    @SneakyThrows
    public void update(FlightPlace flightPlaces) {
        log.info("update(flightPlaces): Received the following 'flightPlaces': " + flightPlaces);
        String sql = "UPDATE FlightPlace SET flight_id = ?, places_econom = ?, places_business = ? WHERE id = ?";

        log.info("update(flightPlaces): Trying to create a connection to data source and prepare a query.");
        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("update(flightPlaces): Putting parameters of specified types into their PreparedStatement positions.");
            ps.setLong  (1, flightPlaces.getFlight().getFlightId());
            ps.setString(2, ServletUtils.stringConversionFromBitSet(flightPlaces.getBitPlacesEconom()));
            ps.setString(3, ServletUtils.stringConversionFromBitSet(flightPlaces.getBitPlacesBusiness()));
            ps.setLong  (4, flightPlaces.getFlightPlacesId());

            log.info("update(flightPlaces): Executing the query: " + ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("update(flightPlaces): SQL exception code: " + e.getErrorCode());
        }
    }

    @Override
    @SneakyThrows
    public void delete(FlightPlace flightPlaces) {
        log.info("delete(flightPlaces): Received the following 'flightPlaces': " + flightPlaces);
        String sql = "DELETE FROM FlightPlace WHERE id = ?";

        log.info("delete(flightPlaces): Trying to create a connection to data source and prepare a query.");
        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("delete(flightPlaces): Putting 'flightPlaces_id' = " + flightPlaces.getFlightPlacesId() + " into its PreparedStatement position.");
            ps.setLong(1, flightPlaces.getFlightPlacesId());

            log.info("delete(flightPlaces): Executing the query: " + ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("delete(flightPlaces): SQL exception code: " + e.getErrorCode());
        }
    }

    @Override
    @SneakyThrows
    public List<FlightPlace> getAll() {
        log.info("getAll(): Creating an empty list of flight places.");
        List<FlightPlace> flightPlaces = new ArrayList<>();
        log.info("getAll(): Trying to create a connection to data source, prepare a query, execute it and put result into ResultSet.");
        try(Connection connection = DataSource.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(SELECT_ALL + ORDER_BY_DATETIME)) {
            log.info("getAll(): Adding flight places from ResultSet to the list.");
            while (rs.next()) {
                flightPlaces.add(createNewFlightPlace(rs));
            }
        } catch (SQLException e) {
            log.error("getAll(): SQL exception code: " + e.getErrorCode());
        }

        log.info("getAll(): Returning the list of flight places.");
        return flightPlaces;
    }

    @SneakyThrows
    private FlightPlace createNewFlightPlace(ResultSet rs) {
        return new FlightPlace(
                rs.getLong("id"),
                new Flight(
                        rs.getLong("flight_id"),
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
                ),
                ServletUtils.bitSetConversionFromString(rs.getString("places_econom")),
                ServletUtils.bitSetConversionFromString(rs.getString("places_business"))
        );
    }
}
