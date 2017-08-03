package db.dao;

import pojo.Invoice;

import java.util.List;
import java.util.Optional;

public interface InvoiceDAO {
    long create(Invoice invoice);
    Optional<Invoice> get(long id);
    void update(Invoice invoice);
    void remove(Invoice invoice);
    List<Invoice> getAll();
}
