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
    public long add(Airplane airplane) {
        String query = "INSERT INTO Airplane (name, capacity_econom, capacity_business) VALUES (?, ?, ?)";

        try (Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, airplane.getName());
            ps.setInt(2, airplane.getCapacityEconom());
            ps.setInt(3, airplane.getCapacityBusiness());

            ps.executeUpdate();

            try (ResultSet generetedKeys = ps.getGeneratedKeys()) {
                if (generetedKeys.next()) {
                    airplane.setAirplaneId(generetedKeys.getInt(1));
                }
            }
        }

        return airplane.getAirplaneId();
    }

    public boolean add(String query) {
        try (Connection connection = DataSource.getConnection();
             Statement stmt = connection.prepareStatement(query)) {
            stmt.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    @SneakyThrows
    public Optional<Airplane> get(int id) {
        String sql = "SELECT name, capacity_econom, capacity_business FROM Airplane WHERE id = ?";

        try (Connection connection = DataSource.getConnection();
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

    public static void main(String[] args) {
        /*AirplaneService as = new AirplaneService();
        Optional<Airplane> airplane = as.get(1);
        airplane.ifPresent(System.out::println);

        System.out.println();
        List<Airplane> list = as.getAll();
        for (Airplane ap: list) {
            System.out.println(ap.toString());
        }*/

        System.out.println("--Flight--");
        FlightService fs = new FlightService();
        List<Flight> flights = fs.getAll();
        for (Flight flight: flights) {
            System.out.println(flight);
        }
    }

    @Override
    public void update(Airplane airplane) {

    }

    @Override
    public void remove(Airplane airplane) {

    }

    @Override
    public List<Airplane> getAll() {
        String sql = "SELECT * FROM airplane";

        List<Airplane> airplanes = new ArrayList<>();
        try(Connection connection = DataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                airplanes.add(new Airplane(
                        result.getLong("id"),
                        result.getString("name"),
                        result.getInt("capacity_econom"),
                        result.getInt("capacity_business")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return airplanes;
    }
}
