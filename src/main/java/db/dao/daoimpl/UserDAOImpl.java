package db.dao.daoimpl;

import db.dao.DataSource;
import db.dao.interfaces.UserDAO;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import pojo.User;
import utils.ServletLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class UserDAOImpl implements UserDAO {
    private static final Logger log = ServletLog.getLgDB();
    private static final String SELECT_ALL = "SELECT id, name, email, password_hash, registration_date FROM Account ";
    private static final String ORDER_BY_REG_DATE = "ORDER BY registration_date";

    private final static UserDAO instance = new UserDAOImpl();

    public static UserDAO getInstance() {
        return instance;
    }

    private UserDAOImpl() {
    }

    @Override
    @SneakyThrows
    public void add(User user) {
        log.info("add(user): Received the following 'user': " + user);
        String sql = "INSERT INTO Account (name, email, password_hash, registration_date) VALUES (?, ?, ?, ?)";

        log.info("add(user): Trying to create a connection to data source and prepare a query.");
        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("add(user): Putting parameters of specified types into their PreparedStatement positions.");
            ps.setString   (1, user.getName());
            ps.setString   (2, user.getEmail());
            ps.setString   (3, user.getPasswordHash());
            ps.setTimestamp(4, Timestamp.valueOf(user.getRegistrationDate()));

            log.info("add(user): Executing the query: " + ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("add(user): SQL exception code: " + e.getErrorCode());
        }
    }

    @Override
    @SneakyThrows
    public Optional<User> get(long id) {
        log.info("get(id): Received the following 'id': " + id);
        String sql = SELECT_ALL + "WHERE id = ?";

        log.info("get(id): Creating a null 'user' object");
        User user = null;
        log.info("get(id): Trying to create a connection to data source and prepare a query.");
        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("get(id): Putting 'id' = " + id + " into its PreparedStatement position.");
            ps.setLong(1, id);

            log.info("get(id): Executing the query and putting result to ResultSet: " + ps);
            ResultSet rs = ps.executeQuery();

            log.info("get(id): Putting ResultSet to 'user' object");
            while (rs.next()) {
                user = createNewUser(rs);
            }
        } catch (SQLException e) {
            log.error("get(id): SQL exception code: " + e.getErrorCode());
        }

        log.info("get(id): Returning a 'user' object: " + user);
        return Optional.ofNullable(user);
    }

    @Override
    @SneakyThrows
    public Optional<User> get(String email) {
        log.info("get(email): Received the following 'email': " + email);
        String sql = SELECT_ALL + "WHERE email = ?";

        log.info("get(email): Creating a null 'user' object");
        User user = null;
        log.info("get(email): Trying to create a connection to data source and prepare a query.");
        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("get(email): Putting 'email' = " + email + " into its PreparedStatement position.");
            ps.setString(1, email);

            log.info("get(email): Trying to execute the query and put result to ResultSet: " + ps);
            try(ResultSet rs = ps.executeQuery()) {
                log.info("get(email): Putting ResultSet to 'user' object");
                while (rs.next()) {
                    user = createNewUser(rs);
                }
            }
        } catch (SQLException e) {
            log.error("get(email): SQL exception code: " + e.getErrorCode());
        }

        log.info("get(id): Returning a 'user' object: " + user);
        return Optional.ofNullable(user);
    }

    @Override
    @SneakyThrows
    public void update(User user) {
        log.info("update(user): Received the following 'user': " + user);
        String sql = "UPDATE Account SET name = ?, email = ?, password_hash = ?, registration_date = ? WHERE id = ?";

        log.info("update(user): Trying to create a connection to data source and prepare a query.");
        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("update(user): Putting parameters of specified types into their PreparedStatement positions.");
            ps.setString   (1, user.getName());
            ps.setString   (2, user.getEmail());
            ps.setString   (3, user.getPasswordHash());
            ps.setTimestamp(3, Timestamp.valueOf(user.getRegistrationDate()));
            ps.setLong     (4, user.getUserId());

            log.info("update(user): Executing the query: " + ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("update(user): SQL exception code: " + e.getErrorCode());
        }
    }

    @Override
    @SneakyThrows
    public void delete(User user) {
        log.info("delete(user): Received the following 'user': " + user);
        String sql = "DELETE FROM Account WHERE id = ?";

        log.info("delete(user): Trying to create a connection to data source and prepare a query.");
        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("delete(user): Putting 'user_id' = " + user.getUserId() + " into its PreparedStatement position.");
            ps.setLong(1, user.getUserId());

            log.info("delete(user): Executing the query: " + ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("delete(user): SQL exception code: " + e.getErrorCode());
        }
    }

    @Override
    @SneakyThrows
    public List<User> getAll() {
        log.info("getAll(): Creating an empty list of users.");
        List<User> users = new ArrayList<>();
        log.info("getAll(): Trying to create a connection to data source, prepare a query, execute it and put result into ResultSet.");
        try(Connection connection = DataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL + ORDER_BY_REG_DATE);
            ResultSet rs = statement.executeQuery()) {
            log.info("getAll(): Adding users from ResultSet to the list.");
            while (rs.next()) {
                users.add(createNewUser(rs));
            }
        } catch (SQLException e) {
            log.error("getAll(): SQL exception code: " + e.getErrorCode());
        }

        log.info("getAll(): Returning the list of users.");
        return users;
    }

    @SneakyThrows
    private User createNewUser(ResultSet rs) {
        return new User(
                rs.getLong     ("id"),
                rs.getString   ("name"),
                rs.getString   ("email"),
                rs.getString   ("password_hash"),
                rs.getTimestamp("registration_date").toLocalDateTime()
        );
    }
}
