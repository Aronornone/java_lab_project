package db.service;

import db.DataSource;
import db.dao.TicketDao;
import lombok.SneakyThrows;
import pojo.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TicketService implements TicketDao {
    private static final String SELECT_ALL =
            "SELECT\n" +
            "  t.id, t.invoice_id, i.account_id, a.name, a.email, a.password_hash, a.registration_date,\n" +
            "  i.status, i.invoice_datetime, flight_id, f.airplane_id, ap.name, ap.capacity_econom, ap.capacity_business, f.flight_number,\n" +
            "  f.departure_airport_id, dep.code, dep.city, dep.airport_name, dep.latitude, dep.longitude,\n" +
            "  f.arrival_airport_id, arr.code, arr.city, arr.airport_name, arr.latitude, arr.longitude,\n" +
            "  f.base_cost, f.available_places_econom, f.available_places_business, f.flight_datetime,\n" +
            "  passenger_name, passport, place, luggage, business_class, price\n" +
            "FROM Ticket t\n" +
            "  JOIN Invoice i   ON i.id = t.invoice_id\n" +
            "  JOIN Account a   ON a.id = i.account_id\n" +
            "  JOIN Flight  f   ON f.id = t.flight_id\n" +
            "  JOIN Airplane ap ON ap.id = f.airplane_id\n" +
            "  JOIN Airport dep ON dep.id = f.departure_airport_id\n" +
            "  JOIN Airport arr ON arr.id = f.arrival_airport_id\n";
    private static final String ORDER_BY_FLIGHT_DATETIME = "ORDER BY f.flight_datetime";


    @SneakyThrows
    public List<Ticket> getTicketsByInvoice(long invoiceId) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = SELECT_ALL + " WHERE t.invoice_id =? "
                + ORDER_BY_FLIGHT_DATETIME;
        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);) {
            ps.setLong(1, invoiceId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tickets.add(createNewTicket(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tickets;
    }

    @Override
    @SneakyThrows
    public long create(Ticket ticket) {
        String sql = "INSERT INTO Ticket (invoice_id, flight_id, passenger_name, passport, place, luggage, business_class, price) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, ticket.getInvoice().getInvoiceId());
            ps.setLong(2, ticket.getFlight().getFlightId());
            ps.setString(3, ticket.getPassengerName());
            ps.setString(4, ticket.getPassport());
            ps.setInt(5, ticket.getSittingPlace());
            ps.setBoolean(6, ticket.isLuggage());
            ps.setBoolean(7, ticket.isBusinessClass());
            ps.setDouble(8, ticket.getPrice());

            ps.executeUpdate();
/*
            try (ResultSet generetedKeys = ps.getGeneratedKeys()) {
                if (generetedKeys.next()) {
                    ticket.setTicketId(generetedKeys.getLong(1));
                }
            }*/
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ticket.getTicketId();
    }

    @Override
    @SneakyThrows
    public Optional<Ticket> get(int id) {
        String sql = SELECT_ALL + "WHERE t.id = ?\n" + ORDER_BY_FLIGHT_DATETIME;

        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);

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
            ps.setLong(1, ticket.getInvoice().getInvoiceId());
            ps.setLong(2, ticket.getFlight().getFlightId());
            ps.setString(3, ticket.getPassengerName());
            ps.setString(4, ticket.getPassport());
            ps.setInt(5, ticket.getSittingPlace());
            ps.setBoolean(6, ticket.isLuggage());
            ps.setBoolean(7, ticket.isBusinessClass());
            ps.setDouble(8, ticket.getPrice());
            ps.setLong(9, ticket.getTicketId());

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
        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL + ORDER_BY_FLIGHT_DATETIME);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                invoices.add(createNewTicket(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return invoices;
    }

    @SneakyThrows
    private Ticket createNewTicket(ResultSet rs) {
        return new Ticket(
                rs.getLong("id"),
                new Invoice(
                        rs.getLong("invoice_id"),
                        new User(
                                rs.getLong     ("account_id"),
                                rs.getString   ("a.name"),
                                rs.getString   ("a.email"),
                                rs.getString   ("a.password_hash"),
                                rs.getTimestamp("registration_date").toLocalDateTime()
                        ),
                        Invoice.InvoiceStatus.valueOf(rs.getString("status")),
                        rs.getTimestamp("invoice_datetime").toLocalDateTime()
                ),
                new Flight(
                        rs.getLong("flight_id"),
                        new Airplane(
                                rs.getLong  ("airplane_id"),
                                rs.getString("ap.name"),
                                rs.getInt   ("ap.capacity_econom"),
                                rs.getInt   ("ap.capacity_business")
                        ),
                        rs.getString("flight_number"),
                        new Airport(
                                rs.getLong  ("departure_airport_id"),
                                rs.getString("dep.code"),
                                rs.getString("dep.city"),
                                rs.getString("dep.airport_name"),
                                rs.getDouble("dep.latitude"),
                                rs.getDouble("dep.longitude")
                        ),
                        new Airport(
                                rs.getLong  ("arrival_airport_id"),
                                rs.getString("arr.code"),
                                rs.getString("arr.city"),
                                rs.getString("arr.airport_name"),
                                rs.getDouble("arr.latitude"),
                                rs.getDouble("arr.longitude")
                        ),
                        rs.getDouble("base_cost"),
                        rs.getInt   ("available_places_econom"),
                        rs.getInt   ("available_places_business"),
                        rs.getTimestamp("flight_datetime").toLocalDateTime()
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
