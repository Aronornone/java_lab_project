package db.service;

import db.DataSource;
import db.dao.AirplaneDAO;
import lombok.SneakyThrows;
import pojo.Airplane;
import pojo.Flight;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AirplaneService implements AirplaneDAO {
    private static final String SELECT_ALL = "SELECT id, name, capacity_econom, capacity_business FROM Airplane";

    @Override
    @SneakyThrows
    public long create(Airplane airplane) {
        String query = "INSERT INTO Airplane (name, capacity_econom, capacity_business) VALUES (?, ?, ?)";

        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, airplane.getName());
            ps.setInt   (2, airplane.getCapacityEconom());
            ps.setInt   (3, airplane.getCapacityBusiness());

            ps.executeUpdate();

            try (ResultSet generetedKeys = ps.getGeneratedKeys()) {
                if (generetedKeys.next()) {
                    airplane.setAirplaneId(generetedKeys.getInt(1));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return airplane.getAirplaneId();
    }

    @Override
    @SneakyThrows
    public Optional<Airplane> get(int id) {
        String sql = "SELECT name, capacity_econom, capacity_business FROM Airplane WHERE id = ?";

        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            Airplane airplane = null;
            while (rs.next()) {
                airplane = new Airplane(id, rs.getString(1), rs.getInt(2), rs.getInt(3));
            }

            return Optional.ofNullable(airplane);
        }
    }

    @Override
    @SneakyThrows
    public void update(Airplane airplane) {
        String sql = "UPDATE Airplane SET name = ?, capacity_econom = ?, capacity_business = ? WHERE id = ?";

        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, airplane.getName());
            ps.setInt   (2, airplane.getCapacityEconom());
            ps.setInt   (3, airplane.getCapacityBusiness());
            ps.setLong  (4, airplane.getAirplaneId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SneakyThrows
    public void remove(Airplane airplane) {
        String sql = "DELETE FROM Airplane WHERE id = ?";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, airplane.getAirplaneId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Airplane> getAll() {
        List<Airplane> airplanes = new ArrayList<>();
        try(Connection connection = DataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL);
            ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                airplanes.add(new Airplane(
                        rs.getLong  ("id"),
                        rs.getString("name"),
                        rs.getInt   ("capacity_econom"),
                        rs.getInt   ("capacity_business")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return airplanes;
    }
}
