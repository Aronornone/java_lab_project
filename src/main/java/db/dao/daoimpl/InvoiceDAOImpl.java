package db.dao.daoimpl;

import db.dao.DataSource;
import db.dao.interfaces.InvoiceDAO;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import pojo.Invoice;
import pojo.User;
import utils.ServletLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class InvoiceDAOImpl implements InvoiceDAO {
    private static final Logger log = ServletLog.getLgDB();
    private static final String SELECT_ALL =
            "SELECT\n" +
            " i.id, account_id, a.name, a.email, a.password_hash, a.registration_date, status, invoice_datetime \n" +
            "FROM Invoice i \n" +
            " JOIN Account a ON a.id = i.account_id \n";
    private static final String ORDER_BY_DATETIME = "ORDER BY invoice_datetime";

    private final static InvoiceDAO instance = new InvoiceDAOImpl();

    public static InvoiceDAO getInstance() {
        return instance;
    }

    private InvoiceDAOImpl() {
    }

    @Override
    @SneakyThrows
    public void add(Invoice invoice) {
        log.info("add(invoice): Received the following 'invoice': " + invoice);
        String sql = "INSERT INTO Invoice (account_id, status, invoice_datetime) VALUES (?, ?, ?)";

        log.info("add(invoice): Trying to create a connection to a data source and prepare a query.");
        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("add(invoice): Putting parameters of specified types into their PreparedStatement positions.");
            ps.setLong     (1, invoice.getUser().getUserId());
            ps.setString   (2, String.valueOf(invoice.getInvoiceStatus()));
            ps.setTimestamp(3, Timestamp.valueOf(invoice.getTimestamp()));

            log.info("add(invoice): Executing the query: " + ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("add(invoice): SQL exception code: " + e.getErrorCode());
        }
    }

    @Override
    @SneakyThrows
    public Optional<Invoice> get(long id) {
        log.info("get(id): Received the following 'id': " + id);
        String sql = SELECT_ALL + "WHERE i.id = ?\n" + ORDER_BY_DATETIME;

        log.info("get(id): Creating a null 'invoice' object");
        Invoice invoice = null;
        log.info("get(id): Trying to create a connection to a data source and prepare a query.");
        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("get(id): Putting 'id' = " + id + " into its PreparedStatement position.");
            ps.setLong(1, id);

            log.info("get(id): Trying to execute the query and put result to ResultSet: " + ps);
            try(ResultSet rs = ps.executeQuery()) {
                log.info("get(id): Creating an 'invoice' object from ResultSet.");
                while (rs.next()) {
                    invoice = createNewInvoce(rs);
                }
            }
        } catch (SQLException e) {
            log.error("get(id): SQL exception code: " + e.getErrorCode());
        }

        log.info("get(id): Returning an 'invoice' object: " + invoice);
        return Optional.ofNullable(invoice);
    }

    @Override
    @SneakyThrows
    public Optional<Invoice> getInvoiceByUser(long userId, Invoice.InvoiceStatus status) {
        log.info("getInvoiceByUser(userId, status): Received the following 'userId': " + userId + ", 'status': " + status);
        String sql = SELECT_ALL + "WHERE i.account_id = ? AND i.status = ?" + ORDER_BY_DATETIME;

        log.info("getInvoiceByUser(userId, status): Creating a null 'invoice' object");
        Invoice invoice = null;
        log.info("getInvoiceByUser(userId, status): Trying to create a connection to a data source and prepare a query.");
        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("getInvoiceByUser(userId, status): Putting parameters of specified types into their PreparedStatement positions.");
            ps.setLong  (1, userId);
            ps.setString(2, status.name());

            log.info("getInvoiceByUser(userId, status): Trying to execute the query and put result to ResultSet: " + ps);
            try(ResultSet rs = ps.executeQuery()) {
                log.info("getInvoiceByUser(userId, status): Creating an 'invoice' object from ResultSet.");
                while (rs.next()) {
                    invoice = createNewInvoce(rs);
                }
            }
        } catch (SQLException e) {
            log.error("getInvoiceByUser(userId, status): SQL exception code: " + e.getErrorCode());
        }

        log.info("getInvoiceByUser(userId, status): Returning an 'invoice' object: " + invoice);
        return Optional.ofNullable(invoice);
    }

    @Override
    @SneakyThrows
    public void update(Invoice invoice) {
        log.info("update(invoice): Received the following 'invoice': " + invoice);
        String sql = "UPDATE Invoice SET account_id = ?, status = ?, invoice_datetime = ? WHERE id = ?";

        log.info("update(invoice): Trying to create a connection to data source and prepare a query.");
        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("update(invoice): Putting parameters of specified types into their PreparedStatement positions.");
            ps.setLong     (1, invoice.getUser().getUserId());
            ps.setString   (2, String.valueOf(invoice.getInvoiceStatus()));
            ps.setTimestamp(3, Timestamp.valueOf(invoice.getTimestamp()));
            ps.setLong     (4, invoice.getInvoiceId());

            log.info("update(invoice): Executing the query: " + ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("update(invoice): SQL exception code: " + e.getErrorCode());
        }
    }

    @Override
    @SneakyThrows
    public void delete(Invoice invoice) {
        log.info("delete(invoice): Received the following 'invoice': " + invoice);
        String sql = "DELETE FROM Invoice WHERE id = ?";

        log.info("delete(invoice): Trying to create a connection to data source and prepare a query.");
        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("delete(invoice): Putting 'invoice_id' = " + invoice.getInvoiceId() + " into its PreparedStatement position.");
            ps.setLong(1, invoice.getInvoiceId());

            log.info("delete(invoice): Executing the query: " + ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("delete(invoice): SQL exception code: " + e.getErrorCode());
        }
    }

    @Override
    @SneakyThrows
    public List<Invoice> getAll() {
        log.info("getAll(): Creating an empty list of invoices.");
        List<Invoice> invoices = new ArrayList<>();
        log.info("getAll(): Trying to create a connection to data source, prepare a query, execute it and put result into ResultSet.");
        try(Connection connection = DataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL + ORDER_BY_DATETIME);
            ResultSet rs = statement.executeQuery()) {
            log.info("getAll(): Adding invoices from ResultSet to the list.");
            while (rs.next()) {
                log.info("getAll(): Creating an 'invoice' object from ResultSet.");
                invoices.add(createNewInvoce(rs));
            }
        } catch (SQLException e) {
            log.error("getAll(): SQL exception code: " + e.getErrorCode());
        }

        log.info("getAll(): Returning the list of invoices.");
        return invoices;
    }

    @Override
    @SneakyThrows
    public List<Invoice> getAllInvoicesByUserAndStatus(long userId, Invoice.InvoiceStatus status) {
        log.info("getAllInvoicesByUserAndStatus(userId, status): Received the following 'userId': " + userId + ", 'status': " + status);
        String sql = SELECT_ALL + "WHERE i.account_id = ? and status =? " + ORDER_BY_DATETIME + " DESC";

        log.info("getAllInvoicesByUserAndStatus(userId, status): Creating an empty list of invoices.");
        List<Invoice> invoices = new ArrayList<>();
        log.info("getAllInvoicesByUserAndStatus(userId, status): Trying to create a connection to data source, prepare a query, execute it and put result into ResultSet.");
        try(Connection connection = DataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("getAllInvoicesByUserAndStatus(userId, status): Putting parameters of specified types into their PreparedStatement positions.");
            ps.setLong  (1, userId);
            ps.setString(2, status.name());

            log.info("getAllInvoicesByUserAndStatus(userId, status): Trying to execute the query and put result to ResultSet: " + ps);
            try(ResultSet rs = ps.executeQuery()) {
                log.info("getAllInvoicesByUserAndStatus(userId, status): Creating an 'invoice' object from ResultSet.");
                while (rs.next()) {
                    invoices.add(createNewInvoce(rs));
                }
            }
        } catch (SQLException e) {
            log.error("getAllInvoicesByUserAndStatus(userId, status): SQL exception code: " + e.getErrorCode());
        }

        log.info("getAllInvoicesByUserAndStatus(userId, status): Returning the list of invoices.");
        return invoices;
    }


    @SneakyThrows
    private Invoice createNewInvoce(ResultSet rs) {
        return new Invoice(
                rs.getLong("id"),
                new User(
                        rs.getLong     ("account_id"),
                        rs.getString   ("a.name"),
                        rs.getString   ("a.email"),
                        rs.getString   ("a.password_hash"),
                        rs.getTimestamp("a.registration_date").toLocalDateTime()
                ),
                Invoice.InvoiceStatus.valueOf(rs.getString("status")),
                rs.getTimestamp("invoice_datetime").toLocalDateTime()
        );
    }
}
