package db.service;

import db.DataSource;
import db.dao.InvoiceDAO;
import lombok.SneakyThrows;
import pojo.Invoice;
import pojo.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InvoiceService implements InvoiceDAO {
    private static final String SELECT_ALL =
            "SELECT\n" +
                    "  i.id, account_id, a.name, a.email, a.password_hash, a.registration_date, status, invoice_datetime\n" +
                    "FROM Invoice i\n" +
                    "  JOIN Account a ON a.id = i.account_id\n";
    private static final String ORDER_BY_DATETIME = "ORDER BY invoice_datetime";

    @Override
    @SneakyThrows
    public long create(Invoice invoice) {
        String sql = "INSERT INTO Invoice (account_id, status, invoice_datetime) VALUES (?, ?, ?)";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, invoice.getUser().getUserId());
            ps.setString(2, String.valueOf(invoice.getInvoiceStatus()));
            ps.setTimestamp(3, Timestamp.valueOf(invoice.getTimestamp()));

            ps.executeUpdate();
/*
            try (ResultSet generetedKeys = ps.getGeneratedKeys()) {
                if (generetedKeys.next()) {
                    invoice.setInvoiceId(generetedKeys.getLong(1));
                }
            }*/
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return invoice.getInvoiceId();
    }

    @Override
    @SneakyThrows
    public Optional<Invoice> get(long id) {
        String sql = SELECT_ALL + "WHERE i.id = ?\n" + ORDER_BY_DATETIME;

        try (Connection connection = DataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            Invoice invoice = null;
            while (rs.next()) {
                invoice = createNewInvoce(rs);
            }

            return Optional.ofNullable(invoice);
        }
    }

    @SneakyThrows
    public Optional<Invoice> getInvoiceByUser(long userId, Invoice.InvoiceStatus status) {
        String sql = SELECT_ALL + "WHERE i.account_id = ? AND i.status = ?" + ORDER_BY_DATETIME;

        try (Connection connection = DataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setString(2, status.name());

            ResultSet rs = ps.executeQuery();

            Invoice invoice = null;
            while (rs.next()) {
                invoice = createNewInvoce(rs);
            }

            return Optional.ofNullable(invoice);
        }
    }


    @Override
    @SneakyThrows
    public void update(Invoice invoice) {
        String sql = "UPDATE Invoice SET account_id = ?, status = ?, invoice_datetime = ? WHERE id = ?";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, invoice.getUser().getUserId());
            ps.setString(2, String.valueOf(invoice.getInvoiceStatus()));
            ps.setTimestamp(3, Timestamp.valueOf(invoice.getTimestamp()));
            ps.setLong(4, invoice.getInvoiceId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SneakyThrows
    public void remove(Invoice invoice) {
        String sql = "DELETE FROM Invoice WHERE id = ?";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, invoice.getInvoiceId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SneakyThrows
    public List<Invoice> getAll() {
        List<Invoice> invoices = new ArrayList<>();
        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL + ORDER_BY_DATETIME);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                invoices.add(createNewInvoce(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return invoices;
    }

    @SneakyThrows
    public List<Invoice> getAllInvoicesByUserAndStatus(long userId, Invoice.InvoiceStatus status) {
        String sql = SELECT_ALL + "WHERE i.account_id = ? and status =? " + ORDER_BY_DATETIME;
        List<Invoice> invoices = new ArrayList<>();
        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            statement.setString(2, status.name());

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                invoices.add(createNewInvoce(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }



    @SneakyThrows
    private Invoice createNewInvoce(ResultSet rs) {
        return new Invoice(
                rs.getLong("id"),
                new User(
                        rs.getLong("account_id"),
                        rs.getString("a.name"),
                        rs.getString("a.email"),
                        rs.getString("a.password_hash"),
                        rs.getTimestamp("a.registration_date").toLocalDateTime()
                ),
                Invoice.InvoiceStatus.valueOf(rs.getString("status")),
                rs.getTimestamp("invoice_datetime").toLocalDateTime()
        );
    }
}
