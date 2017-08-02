package db.service;

import db.DataSource;
import db.dao.FlightPlaceDAO;
import lombok.SneakyThrows;
import pojo.Airplane;
import pojo.Airport;
import pojo.Flight;
import pojo.FlightPlace;
import stubs.StubUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FlightPlaceService implements FlightPlaceDAO {
    private static final String SELECT_ALL =
            "SELECT\n" +
            "  fp.id, f.airplane_id, p.name, p.capacity_econom, p.capacity_business, f.flight_number,\n" +
            "  f.departure_airport_id, d.code, d.city, d.airport_name,d.latitude,d.longitude, f.arrival_airport_id, a.code, a.city, a.airport_name, a.latitude, a.longitude, \n" +
            "  f.base_cost, f.available_places_econom, f.available_places_business, f.flight_datetime,\n" +
            "  places_econom, places_business\n " +
            " FROM FlightPlace fp\n" +
            "  JOIN Flight   f ON f.id = fp.flight_id\n" +
            "  JOIN Airplane p ON p.id = f.airplane_id\n" +
            "  JOIN Airport  d ON d.id = f.departure_airport_id\n" +
            "  JOIN Airport  a ON a.id = f.arrival_airport_id\n";
    private static final String ORDER_BY_DATETIME = " ORDER BY f.flight_datetime ";

    @Override
    @SneakyThrows
    public long create(FlightPlace flightPlaces) {
        String sql = "INSERT INTO FlightPlace (flight_id, places_econom, places_business) " +
                "VALUES (?, ?, ?)";

        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong      (1, flightPlaces.getFlightId().getFlightId());
            ps.setString    (2, String.valueOf(flightPlaces.getBitPlacesEconom()));
            ps.setString    (3, String.valueOf(flightPlaces.getBitPlacesBusiness()));

            ps.executeUpdate();

            try (ResultSet generetedKeys = ps.getGeneratedKeys()) {
                if (generetedKeys.next()) {
                    flightPlaces.setFlightPlacesId(generetedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flightPlaces.getFlightPlacesId();
    }

    @Override
    @SneakyThrows
    public Optional<FlightPlace> get(int id) {
        String sql = SELECT_ALL + "WHERE fp.id = ?\n" + ORDER_BY_DATETIME;

        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            FlightPlace flightPlaces = null;
            while (rs.next()) {
                flightPlaces = createNewFlightPlace(rs);
            }

            return Optional.ofNullable(flightPlaces);
        }
    }

    @Override
    @SneakyThrows
    public Optional<FlightPlace> getByFlight(int flightId) {
        String sql = SELECT_ALL + "WHERE flight_id = ? " + ORDER_BY_DATETIME;

        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, flightId);
            ResultSet rs = ps.executeQuery();

            FlightPlace flightPlaces = null;
            while (rs.next()) {
                flightPlaces = createNewFlightPlace(rs);
            }

            return Optional.ofNullable(flightPlaces);
        }
    }

    @Override
    @SneakyThrows
    public void update(FlightPlace flightPlaces) {
        String sql = "UPDATE FlightPlace SET flight_id = ?, places_econom = ?, places_business = ? WHERE id = ?";

        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong  (1, flightPlaces.getFlightPlacesId());
            ps.setString(2, StubUtils.stringConversionFromBitSet(flightPlaces.getBitPlacesEconom()));
            ps.setString(3, StubUtils.stringConversionFromBitSet(flightPlaces.getBitPlacesBusiness()));
            ps.setLong  (4, flightPlaces.getFlightPlacesId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SneakyThrows
    public void remove(FlightPlace flightPlaces) {
        String sql = "DELETE FROM FlightPlace WHERE id = ?";

        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, flightPlaces.getFlightPlacesId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SneakyThrows
    public List<FlightPlace> getAll() {
        List<FlightPlace> flights = new ArrayList<>();
        try(Connection connection = DataSource.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(SELECT_ALL + ORDER_BY_DATETIME)) {
            while (rs.next()) {
                flights.add(createNewFlightPlace(rs));
            }

            return flights;
        }
    }

    @SneakyThrows
    private FlightPlace createNewFlightPlace(ResultSet rs) {
        return new FlightPlace(
                rs.getLong("id"),
                new Flight(
                        new Airplane(
                                rs.getLong  ("airplane_id"),
                                rs.getString("name"),
                                rs.getInt   ("capacity_econom"),
                                rs.getInt   ("capacity_business")
                                ),
                        rs.getString("flight_number"),
                        new Airport(
                                rs.getLong  ("departure_airport_id"),
                                rs.getString("code"),
                                rs.getString("city"),
                                rs.getString("airport_name"),
                                rs.getDouble("latitude"),
                                rs.getDouble("longitude")
                                ),
                        new Airport(
                                rs.getLong  ("arrival_airport_id"),
                                rs.getString("code"),
                                rs.getString("city"),
                                rs.getString("airport_name"),
                                rs.getDouble("latitude"),
                                rs.getDouble("longitude")
                                ),
                        rs.getDouble   ("base_cost"),
                        rs.getInt      ("available_places_econom"),
                        rs.getInt      ("available_places_business"),
                        rs.getTimestamp("flight_datetime").toLocalDateTime()
                        ),
                StubUtils.bitSetConversionFromString(rs.getString("places_econom")),
                StubUtils.bitSetConversionFromString(rs.getString("places_business"))

        //        BitSet.valueOf(new long[] { Long.parseLong((rs.getString("places_econom")), 2) }),
          //      BitSet.valueOf(new long[] { Long.parseLong((rs.getString("places_business")), 2) })
        );
    }
}
