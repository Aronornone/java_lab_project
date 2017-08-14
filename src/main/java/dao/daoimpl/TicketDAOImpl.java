package dao.daoimpl;

import dao.DataSource;
import dao.interfaces.TicketDAO;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import pojo.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class TicketDAOImpl implements TicketDAO {
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
    private static Logger log = Logger.getLogger("DBLogger");

    private final static TicketDAO instance = new TicketDAOImpl();

    public static TicketDAO getInstance() {
        log.info("getInstance(): Returning instance of TicketDAOImpl.");
        return instance;
    }

    private TicketDAOImpl() {
    }

    @Override
    @SneakyThrows
    public void add(Ticket ticket) {
        log.info("add(ticket): Received the following 'ticket': " + ticket);
        String sql = "INSERT INTO Ticket (invoice_id, flight_id, passenger_name, passport, place, luggage, business_class, price) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        log.info("add(ticket): Trying to create a connection to a data source and prepare a query.");
        try (Connection connection = DataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("add(ticket): Putting parameters of specified types into their PreparedStatement positions.");
            ps.setLong(1, ticket.getInvoice().getInvoiceId());
            ps.setLong(2, ticket.getFlight().getFlightId());
            ps.setString(3, ticket.getPassengerName());
            ps.setString(4, ticket.getPassport());
            ps.setInt(5, ticket.getSittingPlace());
            ps.setBoolean(6, ticket.isLuggage());
            ps.setBoolean(7, ticket.isBusinessClass());
            ps.setDouble(8, ticket.getPrice());

            log.info("add(ticket): Executing the query: " + ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("add(ticket): SQL exception code: " + e.getErrorCode());
        }
    }

    @Override
    @SneakyThrows
    public Optional<Ticket> get(long id) {
        log.info("get(id): Received the following 'id': " + id);
        String sql = SELECT_ALL + "WHERE t.id = ?\n" + ORDER_BY_FLIGHT_DATETIME;

        log.info("get(id): Creating a null 'ticket' object");
        Ticket ticket = null;
        log.info("get(id): Trying to create a connection to a data source and prepare a query.");
        try (Connection connection = DataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("get(id): Putting 'id' = " + id + " into its PreparedStatement position.");
            ps.setLong(1, id);

            log.info("get(id): Trying to execute the query and put result to ResultSet: " + ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    log.info("get(id): Creating a 'ticket' object from ResultSet.");
                    ticket = createNewTicket(rs);
                }
            }
        } catch (SQLException e) {
            log.error("get(id): SQL exception code: " + e.getErrorCode());
        }

        log.info("get(id): Returning a 'ticket' object: " + ticket);
        return Optional.ofNullable(ticket);
    }

    @Override
    @SneakyThrows
    public List<Ticket> getTicketsByInvoice(long invoiceId) {
        log.info("getTicketsByInvoice(invoiceId): Received the following 'invoiceId': " + invoiceId);
        String sql = SELECT_ALL + " WHERE t.invoice_id =? "
                + ORDER_BY_FLIGHT_DATETIME;

        log.info("getTicketsByInvoice(invoiceId): Creating an empty list of tickets.");
        List<Ticket> tickets = new ArrayList<>();
        log.info("getTicketsByInvoice(invoiceId): Trying to create a connection to data source, prepare a query.");
        try (Connection connection = DataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("getTicketsByInvoice(invoiceId): Putting 'invoice_id' = " + invoiceId + " into its PreparedStatement position.");
            ps.setLong(1, invoiceId);

            log.info("getTicketsByInvoice(invoiceId): Trying to execute the query and put result to ResultSet: " + ps);
            try (ResultSet rs = ps.executeQuery()) {
                log.info("getTicketsByInvoice(invoiceId): Adding tickets from ResultSet to the list.");
                while (rs.next()) {
                    tickets.add(createNewTicket(rs));
                }
            }
        } catch (SQLException e) {
            log.error("getTicketsByInvoice(invoiceId): SQL exception code: " + e.getErrorCode());
        }

        log.info("getTicketsByInvoice(invoiceId): Returning the list of tickets.");
        return tickets;
    }

    @Override
    @SneakyThrows
    public void update(Ticket ticket) {
        log.info("update(ticket): Received the following 'ticket': " + ticket);
        String sql = "UPDATE Ticket SET invoice_id = ?, flight_id = ?, passenger_name = ?, passport = ?, place = ?, " +
                "luggage = ?, business_class = ?, price = ? WHERE id = ?";

        log.info("update(ticket): Trying to create a connection to data source and prepare a query.");
        try (Connection connection = DataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("update(ticket): Putting parameters of specified types into their PreparedStatement positions.");
            ps.setLong(1, ticket.getInvoice().getInvoiceId());
            ps.setLong(2, ticket.getFlight().getFlightId());
            ps.setString(3, ticket.getPassengerName());
            ps.setString(4, ticket.getPassport());
            ps.setInt(5, ticket.getSittingPlace());
            ps.setBoolean(6, ticket.isLuggage());
            ps.setBoolean(7, ticket.isBusinessClass());
            ps.setDouble(8, ticket.getPrice());
            ps.setLong(9, ticket.getTicketId());

            log.info("update(ticket): Executing the query: " + ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("update(ticket): SQL exception code: " + e.getErrorCode());
        }
    }

    @Override
    @SneakyThrows
    public void delete(Ticket ticket) {
        log.info("delete(ticket): Received the following 'ticket': " + ticket);
        String sql = "DELETE FROM Ticket WHERE id = ?";

        log.info("delete(ticket): Trying to create a connection to data source and prepare a query.");
        try (Connection connection = DataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("delete(ticket): Putting 'ticket_id' = " + ticket.getTicketId() + " into its PreparedStatement position.");
            ps.setLong(1, ticket.getTicketId());

            log.info("delete(ticket): Executing the query: " + ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("delete(ticket): SQL exception code: " + e.getErrorCode());
        }
    }

    @Override
    @SneakyThrows
    public List<Ticket> getAll() {
        log.info("getAll(): Creating an empty list of tickets.");
        List<Ticket> invoices = new ArrayList<>();
        log.info("getAll(): Trying to create a connection to data source, prepare a query, execute it and put result into ResultSet.");
        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL + ORDER_BY_FLIGHT_DATETIME);
             ResultSet rs = statement.executeQuery()) {
            log.info("getAll(): Adding flight places from ResultSet to the list.");
            while (rs.next()) {
                invoices.add(createNewTicket(rs));
            }
        } catch (SQLException e) {
            log.error("getAll(): SQL exception code: " + e.getErrorCode());
        }

        log.info("getAll(): Returning the list of tickets.");
        return invoices;
    }

    @SneakyThrows
    private Ticket createNewTicket(ResultSet rs) {
        return new Ticket.TicketBuilder(
                new Invoice.InvoiceBuilder(new User(
                        rs.getLong("account_id"),
                        rs.getString("a.name"),
                        rs.getString("a.email"),
                        rs.getString("a.password_hash"),
                        rs.getTimestamp("registration_date").toLocalDateTime()))
                        .invoiceId(rs.getLong("invoice_id"))
                        .invoiceStatus(Invoice.InvoiceStatus.valueOf(rs.getString("status")))
                        .timestamp(rs.getTimestamp("invoice_datetime").toLocalDateTime())
                        .createInvoice(),
                new Flight.FlightBuilder(rs.getLong("flight_id"), rs.getString("flight_number"))
                        .airplane(new Airplane(
                                rs.getLong("airplane_id"),
                                rs.getString("ap.name"),
                                rs.getInt("ap.capacity_econom"),
                                rs.getInt("ap.capacity_business")))
                        .departureAirport(new Airport(
                                rs.getLong("departure_airport_id"),
                                rs.getString("dep.code"),
                                rs.getString("dep.city"),
                                rs.getString("dep.airport_name"),
                                rs.getDouble("dep.latitude"),
                                rs.getDouble("dep.longitude")))
                        .arrivalAirport(new Airport(
                                rs.getLong("arrival_airport_id"),
                                rs.getString("arr.code"),
                                rs.getString("arr.city"),
                                rs.getString("arr.airport_name"),
                                rs.getDouble("arr.latitude"),
                                rs.getDouble("arr.longitude")))
                        .baseCost(rs.getDouble("base_cost"))
                        .availableEconom(rs.getInt("available_places_econom"))
                        .availableBusiness(rs.getInt("available_places_business"))
                        .dateTime(rs.getTimestamp("flight_datetime").toLocalDateTime())
                        .createFlight())
                .ticketId(rs.getLong("id"))
                .passengerName(rs.getString("passenger_name"))
                .passport(rs.getString("passport"))
                .sittingPlace(rs.getInt("place"))
                .luggage(rs.getBoolean("luggage"))
                .business(rs.getBoolean("business_class"))
                .price(rs.getDouble("price"))
                .createTicket();
    }
}
