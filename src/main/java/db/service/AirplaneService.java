package db.service;

import db.DataSource;
import db.dao.AirplaneDAO;
import lombok.SneakyThrows;
import pojo.Airplane;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AirplaneService implements AirplaneDAO {
    private static final String SELECT_ALL = "SELECT id, name, capacity_econom, capacity_business FROM airplane";

    @Override
    @SneakyThrows
    public long add(Airplane airplane) {
        String query = "INSERT INTO airplane (name, capacity_econom, capacity_business) VALUES (?, ?, ?)";

        try (Connection conn = DataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {

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
        try (Connection conn = DataSource.getConnection();
             Statement stmt = conn.prepareStatement(query)) {
            stmt.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    @SneakyThrows
    public Optional<Airplane> get(int id) {
        String query = "SELECT name, capacity_econom, capacity_business FROM airplane WHERE id = ?";

        try (Connection conn = DataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
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
        AirplaneService as = new AirplaneService();
        Optional<Airplane> airplane = as.get(55);
        airplane.ifPresent(System.out::println);

        System.out.println();
        List<Airplane> list = as.getAll();
        for (Airplane ap: list) {
            System.out.println(ap.toString());
        }
    }

    @Override
    public void update(Airplane airplane) {

    }

    @Override
    public void remove(Airplane airplane) {

    }

    @Override
    @SneakyThrows
    public List<Airplane> getAll() {
        List<Airplane> airplanes = new ArrayList<>();

        try (Connection conn = DataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL)) {
            while (rs.next()) {
                airplanes.add(new Airplane(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("capacity_econom"),
                        rs.getInt("capacity_business")
                ));
            }
        }

        return airplanes;
    }
}
