package db.service;

import db.DataSource;
import db.dao.AirportDAO;
import lombok.SneakyThrows;
import pojo.Airport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AirportService implements AirportDAO {
    private static final String SELECT_ALL = "SELECT id, name, city FROM Airport";

    @Override
    @SneakyThrows
    public long create(Airport airport) {
        String query = "INSERT INTO Airport (name, city) VALUES (?, ?)";

        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, airport.getName());
            ps.setString(2, airport.getCity());

            ps.executeUpdate();

            try (ResultSet generetedKeys = ps.getGeneratedKeys()) {
                if (generetedKeys.next()) {
                    airport.setAirportId(generetedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return airport.getAirportId();
    }

    @Override
    @SneakyThrows
    public Optional<Airport> get(int id) {
        String sql = "SELECT name, city FROM Airport WHERE id = ?";

        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            Airport airport = null;
            while (rs.next()) {
                airport = new Airport(id, rs.getString(1), rs.getString(2));
            }

            return Optional.ofNullable(airport);
        }
    }

    @Override
    @SneakyThrows
    public void update(Airport airport) {
        String sql = "UPDATE Airport SET name = ?, city = ? WHERE id = ?";

        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, airport.getName());
            ps.setString(2, airport.getCity());
            ps.setLong  (3, airport.getAirportId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SneakyThrows
    public void remove(Airport airport) {
        String sql = "DELETE FROM Airport WHERE id = ?";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, airport.getAirportId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SneakyThrows
    public List<Airport> getAll() {
        List<Airport> airports = new ArrayList<>();
        try(Connection connection = DataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL);
            ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                airports.add(new Airport(
                        result.getLong  ("id"),
                        result.getString("name"),
                        result.getString("city")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return airports;
    }
}
