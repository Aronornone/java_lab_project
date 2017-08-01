package db.service;

import db.DataSource;
import db.dao.TicketDao;
import lombok.SneakyThrows;
import pojo.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TicketService implements TicketDao {
    private static final String SELECT_ALL = "SELECT t.id, invoice_id, i.account_id, a.name, a.email, a.password_hash, a.registration_date, " +
            "i.status, i.invoice_datetime, flight_id, f.airplane_id, ap.name, ap.capacity_econom, ap.capacity_business, f.flight_number, " +
            "f.departure_airport_id, dep.code, dep.city, dep.airport_name, dep.latitude, dep.longitude, " +
            "f.arrival_airport_id, dep.code, dep.city, dep.airport_name, dep.latitude, dep.longitude, " +
            "f.base_cost, f.available_places_econom, f.available_places_business, f.flight_datetime, " +
            "passenger_name, passport, place, luggage, business_class, price " +
            "FROM Ticket t, Invoice i, Account a, Flight f, Airplane ap, Airport dep, Airport arr ";
    private static final String ORDER_BY_FLIGHT_DATETIME = "ORDER BY f.flight_datetime";

    @Override
    @SneakyThrows
    public long create(Ticket ticket) {
        String sql = "INSERT INTO Ticket (invoice_id, flight_id, passenger_name, passport, place, luggage, business_class, price) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong   (1, ticket.getInvoice().getInvoiceId());
            ps.setLong   (2, ticket.getFlight().getFlightId());
            ps.setString (3, ticket.getPassengerName());
            ps.setString (4, ticket.getPassport());
            ps.setInt    (5, ticket.getSittingPlace());
            ps.setBoolean(6, ticket.isLuggage());
            ps.setBoolean(7, ticket.isBusinessClass());
            ps.setDouble (8, ticket.getPrice());

            ps.executeUpdate();

            try (ResultSet generetedKeys = ps.getGeneratedKeys()) {
                if (generetedKeys.next()) {
                    ticket.setTicketId(generetedKeys.getInt(1));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ticket.getTicketId();
    }

    @Override
    @SneakyThrows
    public Optional<Ticket> get(int id) {
        String sql = SELECT_ALL + "WHERE t.id = ? " + ORDER_BY_FLIGHT_DATETIME;

        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            Ticket ticket = null;
            while (rs.next()) {
                ticket = createNewTicket(rs);
            }

            return Optional.ofNullable(ticket);
        }
    }

    @Override
    @SneakyThrows
    public void update(Ticket ticket) {
        String sql = "UPDATE Ticket SET invoice_id = ?, flight_id = ?, passenger_name = ?, passport = ?, place = ?, " +
            "luggage = ?, business_class = ?, price = ? WHERE id = ?";

        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong   (1, ticket.getInvoice().getInvoiceId());
            ps.setLong   (2, ticket.getFlight().getFlightId());
            ps.setString (3, ticket.getPassengerName());
            ps.setString (4, ticket.getPassport());
            ps.setInt    (5, ticket.getSittingPlace());
            ps.setBoolean(6, ticket.isLuggage());
            ps.setBoolean(7, ticket.isBusinessClass());
            ps.setDouble (8, ticket.getPrice());
            ps.setLong   (9, ticket.getTicketId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SneakyThrows
    public void remove(Ticket ticket) {
        String sql = "DELETE FROM Ticket WHERE id = ?";

        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, ticket.getTicketId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SneakyThrows
    public List<Ticket> getAll() {
        List<Ticket> invoices = new ArrayList<>();
        try(Connection connection = DataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL + ORDER_BY_FLIGHT_DATETIME);
            ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                invoices.add(createNewTicket(rs));
            }
        } catch(SQLException e){
            e.printStackTrace();
        }

        return invoices;
    }

    @SneakyThrows
    private Ticket createNewTicket(ResultSet rs) {
        return new Ticket(
                rs.getLong("id"),
                new Invoice(
                        rs.getLong("id"),
                        new User(
                                rs.getLong      ("user_id"),
                                rs.getString    ("name"),
                                rs.getString    ("email"),
                                rs.getString    ("password_hash"),
                                rs.getTimestamp ("registration_date").toLocalDateTime()
                        ),
                        Invoice.InvoiceStatus.valueOf(rs.getString("status")),
                        rs.getTimestamp("invoice_datetime").toLocalDateTime()
                ),
                new Flight(
                    rs.getLong("id"),
                    new Airplane(
                            rs.getLong  ("airplane_id"),
                            rs.getString("name"),
                            rs.getInt   ("capacity_econom"),
                            rs.getInt   ("capacity_business")
                    ),
                    rs.getString("flight_number"),
                    new Airport(
                            rs.getLong  ("airport_id"),
                            rs.getString("code"),
                            rs.getString("city"),
                            rs.getString("airport_name"),
                            rs.getDouble("latitude"),
                            rs.getDouble("longitude")
                    ),
                    new Airport(
                            rs.getLong  ("airport_id"),
                            rs.getString("code"),
                            rs.getString("city"),
                            rs.getString("airport_name"),
                            rs.getDouble("latitude"),
                            rs.getDouble("longitude")
                    ),
                    rs.getDouble    ("base_cost"),
                    rs.getInt       ("available_places_econom"),
                    rs.getInt       ("available_places_business"),
                    rs.getTimestamp ("flight_datetime").toLocalDateTime()
                ),
                rs.getString ("passenger_name"),
                rs.getString ("passport"),
                rs.getInt    ("place"),
                rs.getBoolean("luggage"),
                rs.getBoolean("business_class"),
                rs.getDouble ("price")
            );
    }
}
