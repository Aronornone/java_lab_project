package db.service;

import db.DataSource;
import db.dao.UserDAO;
import lombok.SneakyThrows;
import pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public class UserService implements UserDAO {
    @Override
    public long create(User user) {
        return 0;
    }

    @Override
    @SneakyThrows

    public Optional<User> get(int id) {
        String sql = "SELECT name, email, password_hash, registration_date FROM user WHERE id =   ?";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            User user = null;
            while (rs.next()) {
                user = new User(id, rs.getString(1), rs.getString(2), rs.getString(3),
                        rs.getTimestamp(4).toLocalDateTime());
            }
            return Optional.ofNullable(user);
        }
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void remove(User user) {

    }

    @Override
    public List<User> getAll() {
        return null;
    }
}
