package db.dao.daoimpl;

import db.dao.DataSource;
import db.dao.interfaces.AirplaneDAO;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import pojo.Airplane;
import utils.ServletLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AirplaneDAOImpl implements AirplaneDAO {
    private static final String SELECT_ALL = "SELECT id, name, capacity_econom, capacity_business FROM Airplane ";
    private static final Logger log = ServletLog.getLgDB();

    private final static AirplaneDAO instance = new AirplaneDAOImpl();

    public static AirplaneDAO getInstance() {
        return instance;
    }

    private AirplaneDAOImpl() {
    }

    @Override
    @SneakyThrows
    public void add(Airplane airplane) {
        log.info("add(airplane): Received the following 'airplane': " + airplane);
        String sql = "INSERT INTO Airplane (name, capacity_econom, capacity_business) VALUES (?, ?, ?)";

        log.info("add(airplane): Trying to create connection to data source and prepare a query.");
        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("add(airplane): Putting parameters of specified types into their PreparedStatement positions.");
            ps.setString(1, airplane.getName());
            ps.setInt   (2, airplane.getCapacityEconom());
            ps.setInt   (3, airplane.getCapacityBusiness());

            log.info("add(airplane): Executing the query: " + ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("add(airport): SQL exception!\n" + e);
        }
    }

    @Override
    @SneakyThrows
    public Optional<Airplane> get(long id) {
        log.info("get(id): Received the following 'id': " + id);
        String sql = SELECT_ALL + "WHERE id = ?";

        log.info("get(id): Creating a null 'airplane' object");
        Airplane airplane = null;
        log.info("get(id): Trying to create a connection to a data source and prepare a query.");
        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("get(id): Putting 'id' = " + id + " into its PreparedStatement position.");
            ps.setLong(1, id);

            log.info("get(id): Executing the query and putting result to ResultSet: " + ps);
            ResultSet rs = ps.executeQuery();

            log.info("get(id): Creating an 'airplane' object from ResultSet.");
            while (rs.next()) {
                airplane = createNewAirplane(rs);
            }
        } catch (SQLException e) {
            log.error("get(id): SQL exception!\n" + e);
        }

        log.info("get(id): Returning an 'airplane' object: " + airplane);
        return Optional.ofNullable(airplane);
    }

    @Override
    @SneakyThrows
    public void update(Airplane airplane) {
        log.info("update(airplane): Received the following 'airplane': " + airplane);
        String sql = "UPDATE Airplane SET name = ?, capacity_econom = ?, capacity_business = ? WHERE id = ?";

        log.info("update(airplane): Trying to create a connection to data source and prepare a query.");
        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("update(airplane): Putting parameters of specified types into their PreparedStatement positions.");
            ps.setString(1, airplane.getName());
            ps.setInt   (2, airplane.getCapacityEconom());
            ps.setInt   (3, airplane.getCapacityBusiness());
            ps.setLong  (4, airplane.getAirplaneId());

            log.info("update(airplane): Executing the query: " + ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("update(airplane): SQL exception!\n" + e);
        }
    }

    @Override
    @SneakyThrows
    public void delete(Airplane airplane) {
        log.info("delete(airplane): Received the following 'airplane': " + airplane);
        String sql = "DELETE FROM Airplane WHERE id = ?";

        log.info("delete(airplane): Trying to create a connection to data source and prepare a query.");
        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("delete(airplane): Putting 'airplane_id' = " + airplane.getAirplaneId() + " into its PreparedStatement position.");
            ps.setLong(1, airplane.getAirplaneId());

            log.info("delete(airplane): Executing the query: " + ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("delete(airplane): SQL exception!\n" + e);
        }
    }

    @Override
    public List<Airplane> getAll() {
        log.info("getAll(): Creating an empty list of airplanes.");
        List<Airplane> airplanes = new ArrayList<>();
        log.info("getAll(): Trying to create a connection to data source, prepare a query, execute it and put result into ResultSet.");
        try(Connection connection = DataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL);
            ResultSet rs = statement.executeQuery()) {
            log.info("getAll(): Adding airplanes from ResultSet to the list.");
            while (rs.next()) {
                airplanes.add(createNewAirplane(rs));
            }
        } catch (SQLException e) {
            log.error("getAll(): SQL exception!\n" + e);
        }

        log.info("getAll(): Returning the list of airplanes.");
        return airplanes;
    }

    @SneakyThrows
    private Airplane createNewAirplane(ResultSet rs) {
        return new Airplane(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getInt("capacity_econom"),
                rs.getInt("capacity_business"));
    }
}
