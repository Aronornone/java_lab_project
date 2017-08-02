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
    private static final String SELECT_ALL = "SELECT id, code, city, airport_name, latitude, longitude FROM Airport ";

    @Override
    @SneakyThrows
    public long create(Airport airport) {
        String query = "INSERT INTO Airport (code, city,airport_name, latitude, longitude) VALUES (?, ?, ?, ?, ?)";

        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, airport.getCode());
            ps.setString(2, airport.getCity());
            ps.setString(3, airport.getAirportName());
            ps.setDouble(4, airport.getLatitude());
            ps.setDouble(5, airport.getLongitude());

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
        String sql = SELECT_ALL + "WHERE id = ?";

        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            Airport airport = null;
            while (rs.next()) {
                airport = new Airport(id, rs.getString(2), rs.getString(3),rs.getString(4),rs.getDouble(5),rs.getDouble(6));
            }

            return Optional.ofNullable(airport);
        }
    }

    @Override
    @SneakyThrows
    public void update(Airport airport) {
        String sql = "UPDATE Airport SET code = ?, city = ?, airport_name = ?, latitude = ?, longitude = ? WHERE id = ?";

        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, airport.getCode());
            ps.setString(2, airport.getCity());
            ps.setString(3, airport.getAirportName());
            ps.setDouble(4, airport.getLatitude());
            ps.setDouble(5, airport.getLongitude());
            ps.setLong  (6, airport.getAirportId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SneakyThrows
    public void remove(Airport airport) {
        String sql = "DELETE FROM Airport WHERE id = ?";

        try(Connection connection = DataSource.getConnection();
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
        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                airports.add(new Airport(
                        result.getLong  ("id"),
                        result.getString("code"),
                        result.getString("city"),
                        result.getString("airport_name"),
                        result.getDouble("latitude"),
                        result.getDouble("longitude")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return airports;
    }
}
