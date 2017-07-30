package db.service;

import db.DataSource;
import db.dao.AirplaneDAO;
import db.dao.AirportDAO;
import pojo.Airplane;
import pojo.Airport;

import javax.swing.text.html.Option;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AirportService implements AirportDAO {
    @Override
    public long add(Airport airport) {
        return 0;
    }

    @Override
    public Optional<Airport> get(int id) {
      return null;
    }

    @Override
    public void update(Airport airport) {

    }

    @Override
    public void remove(Airport airport) {

    }

    @Override
    public List<Airport> getAll() {
        String sql = "SELECT * FROM airport";

        List<Airport> airports = new ArrayList<>();
        try(Connection connection = DataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                airports.add(new Airport(
                        result.getLong("id"),
                        result.getString("name"),
                        result.getString("city")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return airports;
    }
}
