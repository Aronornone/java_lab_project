package db.service;

import db.DataSource;
import db.dao.FlightDAO;
import lombok.SneakyThrows;
import pojo.Airplane;
import pojo.Airport;
import pojo.Flight;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class FlightService implements FlightDAO {
    private static final String SELECT_ALL = "SELECT f.id, airplane_id, p.name, p.capacity_econom, p.capacity_business, flight_number, " +
        "departure_airport_id, d.name, d.city, arrival_airport_id, a.name, a.city, " +
        "base_cost, available_places_econom, available_places_business, flight_datetime " +
        "FROM Flight f, Airplane p, Airport d, Airport a ";
    private static final String ORDER_BY_DATETIME = "ORDER BY flight_datetime";

    @Override
    @SneakyThrows
    public long create(Flight flight) {
        String sql = "INSERT INTO Flight (airplane_id, flight_number, departure_airport_id, arrival_airport_id, " +
            "base_cost, available_places_econom, available_places_business, flight_datetime) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong      (1, flight.getAirplane().getAirplaneId());
            ps.setString    (2, flight.getFlightNumber());
            ps.setLong      (3, flight.getDepartureAirport().getAirportId());
            ps.setLong      (4, flight.getArrivalAirport().getAirportId());
            ps.setDouble    (5, flight.getBaseCost());
            ps.setInt       (6, flight.getAvailablePlacesEconom());
            ps.setInt       (7, flight.getAvailablePlacesBusiness());
            ps.setTimestamp (8, Timestamp.valueOf(flight.getDateTime()));

            ps.executeUpdate();

            try (ResultSet generetedKeys = ps.getGeneratedKeys()) {
                if (generetedKeys.next()) {
                    flight.setFlightId(generetedKeys.getInt(1));
                }
            }
        }

        return flight.getFlightId();
    }

    @Override
    @SneakyThrows
    public Optional<Flight> get(int id) {
        String sql = SELECT_ALL + " WHERE f.id = ?";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            Flight flight = null;
            while (rs.next()) {
                flight = createNewFlight(rs);
            }

            return Optional.ofNullable(flight);
        }
    }

    @SneakyThrows
    public Optional<Flight> get(Airport departureCity, Airport arrivalCity, LocalDateTime dateTime, int availablePlaces, boolean econom) {
        String sql = SELECT_ALL + " WHERE d.city = ?, a.city = ?, flight_datetime = ?, ";
        if (econom) sql += "available_places_econom - ? >= 0";
        else        sql += "available_places_business - ? >= 0";

        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString    (1, departureCity.getCity());
            ps.setString    (2, arrivalCity.getCity());
            ps.setInt       (3, availablePlaces);
            ps.setTimestamp (3, Timestamp.valueOf(dateTime));

            ResultSet rs = ps.executeQuery();

            Flight flight = null;
            while (rs.next()) {
                flight = createNewFlight(rs);
            }

            return Optional.ofNullable(flight);
        }
    }

    @Override
    @SneakyThrows
    public void update(Flight flight) {
        String sql = "UPDATE Flight SET airplane_id = ?, flight_number = ?, departure_airport_id = ?, arrival_airport_id =?, " +
                "base_cost = ?, available_places_econom = ?, available_places_business = ?, flight_datetime = ? WHERE id = ?";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong      (1, flight.getAirplane().getAirplaneId());
            ps.setString    (2, flight.getFlightNumber());
            ps.setLong      (3, flight.getDepartureAirport().getAirportId());
            ps.setLong      (4, flight.getArrivalAirport().getAirportId());
            ps.setDouble    (5, flight.getBaseCost());
            ps.setInt       (6, flight.getAvailablePlacesEconom());
            ps.setInt       (7, flight.getAvailablePlacesBusiness());
            ps.setTimestamp (8, Timestamp.valueOf(flight.getDateTime()));
            ps.setLong      (9, flight.getFlightId());

            ps.executeUpdate();
        }
    }

    @Override
    @SneakyThrows
    public void remove(Flight flight) {
        String sql = "DELETE FROM Flight WHERE id = ?";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, flight.getFlightId());

            ps.executeUpdate();
        }
    }

    @Override
    @SneakyThrows
    public List<Flight> getAll() {
        List<Flight> flights = new ArrayList<>();
        try(Connection connection = DataSource.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(SELECT_ALL + ORDER_BY_DATETIME)) {
            while (rs.next()) {
                flights.add(createNewFlight(rs));
            }

            return flights;
        }
    }

    @SneakyThrows
    private Flight createNewFlight(ResultSet rs) {
        return new Flight(rs.getLong("id"),
                new Airplane(
                        rs.getLong("airplane_id"),
                        rs.getString("name"),
                        rs.getInt("capacity_econom"),
                        rs.getInt("capacity_business")
                ),
                rs.getString("flight_nunmber"),
                new Airport(
                        rs.getLong("airport_id"),
                        rs.getString("name"),
                        rs.getString("city")
                ),
                new Airport(
                        rs.getLong("airport_id"),
                        rs.getString("name"),
                        rs.getString("city")
                ),
                rs.getDouble("base_cost"),
                rs.getInt("available_places_econom"),
                rs.getInt(" available_places_business"),
                rs.getTimestamp("flight_datetime").toLocalDateTime()
        );
    }
}
