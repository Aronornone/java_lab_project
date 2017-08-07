package db.services.interfaces;

import pojo.Invoice;

import java.util.List;
import java.util.Optional;

public interface InvoiceService {
    void add(Invoice invoice);
    Optional<Invoice> get(long id);
    Optional<Invoice> getInvoiceByUser(long userId, Invoice.InvoiceStatus status);
    void update(Invoice invoice);
    void delete(Invoice invoice);
    List<Invoice> getAll();
    List<Invoice> getAllInvoicesByUserAndStatus(long userId, Invoice.InvoiceStatus status);
}
