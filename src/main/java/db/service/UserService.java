package db.service;

import db.DataSource;
import db.dao.UserDAO;
import lombok.SneakyThrows;
import pojo.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService implements UserDAO {
    private static final String SELECT_ALL = "SELECT id, name, email, password_hash, registration_date FROM Account ";
    private static final String ORDER_BY_REG_DATE = "ORDER BY registration_date";

    @Override
    @SneakyThrows
    public long create(User user) {
        String sql = "INSERT INTO Account (name, email, password_hash, registration_date) VALUES (?, ?, ?, ?)";
        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString   (1, user.getName());
            ps.setString   (2, user.getEmail());
            ps.setString   (3, user.getPasswordHash());
            ps.setTimestamp(4, Timestamp.valueOf(user.getRegistrationDate()));

            ps.executeUpdate();

            try (ResultSet generetedKeys = ps.getGeneratedKeys()) {
                if (generetedKeys.next()) {
                    user.setUserId(generetedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user.getUserId();
    }

    @Override
    @SneakyThrows
    public Optional<User> get(long id) {
        String sql = "SELECT name, email, password_hash, registration_date FROM Account WHERE id = ?";

        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            User user = null;
            while (rs.next()) {
                user = createNewUser(rs);
            }
            return Optional.ofNullable(user);
        }
    }

    @Override
    @SneakyThrows
    public Optional<User> get(String email) {
        String sql = SELECT_ALL + "WHERE email = ?";

        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            User user = null;
            while (rs.next()) {
                user = createNewUser(rs);
            }
            return Optional.ofNullable(user);
        }
    }

    @Override
    @SneakyThrows
    public void update(User user) {
        String sql = "UPDATE Account SET name = ?, email = ?, password_hash = ?, registration_date = ? WHERE id = ?";

        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString   (1, user.getName());
            ps.setString   (2, user.getEmail());
            ps.setString   (3, user.getPasswordHash());
            ps.setTimestamp(3, Timestamp.valueOf(user.getRegistrationDate()));
            ps.setLong     (4, user.getUserId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SneakyThrows
    public void remove(User user) {
        String sql = "DELETE FROM Account WHERE id = ?";

        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, user.getUserId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SneakyThrows
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try(Connection connection = DataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL + ORDER_BY_REG_DATE);
            ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                users.add(createNewUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

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
