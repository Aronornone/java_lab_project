package db.dao.daoimpl;

import db.dao.DataSource;
import db.dao.interfaces.AirportDAO;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import pojo.Airport;
import utils.ServletLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class AirportDAOImpl implements AirportDAO {
    private static final String SELECT_ALL = "SELECT id, code, city, airport_name, latitude, longitude FROM Airport ";
    private static final Logger log = ServletLog.getLgDB();

    private final static AirportDAO instance = new AirportDAOImpl();

    public static AirportDAO getInstance() {
        return instance;
    }

    private AirportDAOImpl() {
    }

    @Override
    @SneakyThrows
    public void add(Airport airport) {
        log.info("add(airport): Received the following 'airport': " + airport);
        String sql = "INSERT INTO Airport (code, city, airport_name, latitude, longitude) VALUES (?, ?, ?, ?, ?)";

        log.info("add(airport): Trying to create connection to data source and prepare a query.");
        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("add(airport): Putting parameters of specified types into their PreparedStatement positions.");
            ps.setString(1, airport.getCode());
            ps.setString(2, airport.getCity());
            ps.setString(3, airport.getAirportName());
            ps.setDouble(4, airport.getLatitude());
            ps.setDouble(5, airport.getLongitude());

            log.info("add(airport): Executing the query: " + ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("add(airport): SQL exception!\n" + e);
        }
    }

    @Override
    @SneakyThrows
    public Optional<Airport> get(long id) {
        log.info("get(id): Received the following 'id': " + id);
        String sql = SELECT_ALL + "WHERE id = ?";

        log.info("get(id): Creating a null 'airport' object");
        Airport airport = null;
        log.info("get(id): Trying to create a connection to data source and prepare a query.");
        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("get(id): Putting 'id' = " + id + " into its PreparedStatement position.");
            ps.setLong(1, id);

            log.info("get(id): Executing the query and putting result to ResultSet: " + ps);
            ResultSet rs = ps.executeQuery();

            log.info("get(id): Creating an 'airport' object from ResultSet.");
            while (rs.next()) {
                airport = createNewAirport(rs);
            }
        } catch (SQLException e) {
            log.error("get(id): SQL exception!\n" + e);
        }

        log.info("get(id): Returning an 'airport' object: " + airport);
        return Optional.ofNullable(airport);
    }

    @Override
    @SneakyThrows
    public void update(Airport airport) {
        log.info("update(airport): Received the following 'airport': " + airport);
        String sql = "UPDATE Airport SET code = ?, city = ?, airport_name = ?, latitude = ?, longitude = ? WHERE id = ?";

        log.info("update(airport): Trying to create a connection to a data source and prepare a query.");
        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("update(airport): Putting parameters of specified types into their PreparedStatement positions.");
            ps.setString(1, airport.getCode());
            ps.setString(2, airport.getCity());
            ps.setString(3, airport.getAirportName());
            ps.setDouble(4, airport.getLatitude());
            ps.setDouble(5, airport.getLongitude());
            ps.setLong  (6, airport.getAirportId());

            log.info("update(airport): Executing the query: " + ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("update(airport): SQL exception!\n" + e);
        }
    }

    @Override
    @SneakyThrows
    public void delete(Airport airport) {
        log.info("delete(airport): Received the following 'airport': " + airport);
        String sql = "DELETE FROM Airport WHERE id = ?";

        log.info("delete(airport): Trying to create a connection to data source and prepare a query.");
        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("delete(airport): Putting 'airport_id' = " + airport.getAirportId() + " into its PreparedStatement position.");
            ps.setLong(1, airport.getAirportId());

            log.info("delete(airport): Executing the query: " + ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("delete(user): SQL exception!\n" + e);
        }
    }

    @Override
    public List<Airport> getAll() {
        log.info("getAll(): Creating an empty list of airports.");
        List<Airport> airports = new ArrayList<>();
        log.info("getAll(): Trying to create a connection to a data source, prepare a query, execute it and put result into ResultSet.");
        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL);
             ResultSet result = statement.executeQuery()) {
            log.info("getAll(): Adding airports from ResultSet to the list.");
            while (result.next()) {
                airports.add(createNewAirport(result));
            }
        } catch (SQLException e) {
            log.error("getAll(): SQL exception!\n" + e);
        }

        log.info("getAll(): Returning the list of airports.");
        return airports;
    }

    @SneakyThrows
    private Airport createNewAirport(ResultSet rs) {
        return new Airport(
                rs.getLong  ("id"),
                rs.getString("code"),
                rs.getString("city"),
                rs.getString("airport_name"),
                rs.getDouble("latitude"),
                rs.getDouble("longitude"));
    }
}
